#include "p3160153-p3140290-p3160194-pizza2.h"

void check_return_code(int return_code, char r);


pthread_mutex_t mutex_available_cooks;
pthread_mutex_t mutex_available_ovens;
pthread_mutex_t mutex_available_deliverer;
pthread_mutex_t mutex_count;
pthread_mutex_t mutex_console;
pthread_mutex_t mutex_total_order_completion;
pthread_mutex_t mutex_sum;
pthread_mutex_t mutex_avg;


pthread_cond_t cond_available_cooks;
pthread_cond_t cond_available_ovens;
pthread_cond_t cond_available_deliverer;

int available_cooks;
int available_ovens;
int available_deliverer;
unsigned int seed;
int count=-1;
double *orders;

double sum_X;
double sum_Y;

double* max_delivery;
double* max_cool;


void * execute(void * customer_id)
{
  int *cid;
  cid = (int *)customer_id;
  int rc;
  struct timespec order_start;
  struct timespec prep_start;
  struct timespec bake_start;
  struct timespec order_done;
  struct timespec order_delivered;
  double waiting_duration;
  double prep_duration;
  double bake_duration;
  double delivery_duration;
  double time_X;
  double time_Y;

  //customer is waiting

  //If count==0 it is the first order so we accept it the order right away
  rc = pthread_mutex_lock(&mutex_count);
  check_return_code(rc, 'l');
  count++;
  rc = pthread_mutex_unlock(&mutex_count);
  check_return_code(rc, 'u');
  //Starts accepting order after a random time
  if(count!= 0){
    int accept_delay_time = (rand_r(&seed) % (order_high - order_low +1))+order_low;
    sleep(accept_delay_time);
  }


  clock_gettime(CLOCK_REALTIME, &order_start); //1: track time when an order is recieved
  //order accepted

  rc = pthread_mutex_lock(&mutex_available_cooks);
  check_return_code(rc, 'l');

  while (available_cooks <= 0) {
    pthread_cond_wait(&cond_available_cooks,  &mutex_available_cooks);
  }
  //order gets assigned to a cook
  --available_cooks;

  clock_gettime(CLOCK_REALTIME, &prep_start); //2: track time when order goes to a cook
  rc = pthread_mutex_unlock(&mutex_available_cooks);
  check_return_code(rc, 'u');

  //Cook: starts preparing the order
  int number_of_pizzas = (rand_r(&seed) % (max_order - min_order +1))+min_order; //number of pizzas of an order
  for(int i = 0; i < number_of_pizzas; i++){
    sleep(T_prep);
  }

  //Cook waits until an oven becomes available
  rc = pthread_mutex_lock(&mutex_available_ovens);
  check_return_code(rc, 'l');

  while (available_ovens <= 0) {
   pthread_cond_wait(&cond_available_ovens,  &mutex_available_ovens);
  }
  --available_ovens;

  clock_gettime(CLOCK_REALTIME, &bake_start); //3: track time when order is baking
  rc = pthread_mutex_unlock(&mutex_available_ovens);
  check_return_code(rc, 'u');


  //Release cook
  rc = pthread_mutex_lock(&mutex_available_cooks);
  check_return_code(rc, 'l');

  ++available_cooks;

  pthread_cond_signal(&cond_available_cooks);
  rc = pthread_mutex_unlock(&mutex_available_cooks);
  check_return_code(rc, 'u');

  //All pizzas of the same order are baking in the same oven for T_bake
  sleep(T_bake);

  //Asiign to a deliverer
  rc = pthread_mutex_lock(&mutex_available_deliverer);
  check_return_code(rc, 'l');
  while (available_deliverer <= 0) {
   pthread_cond_wait(&cond_available_deliverer, &mutex_available_deliverer);
  }
  --available_deliverer;

  clock_gettime(CLOCK_REALTIME, &order_done); //4: track time when  order is baked and waiting for delivery
  rc = pthread_mutex_unlock(&mutex_available_deliverer);
  check_return_code(rc, 'u');

  /*
  * When an order is assigned to a delivered, then the oven gets released
  */
  //Release oven
  rc = pthread_mutex_lock(&mutex_available_ovens);
  check_return_code(rc, 'l');

	++available_ovens;

	pthread_cond_signal(&cond_available_ovens);
	rc = pthread_mutex_unlock(&mutex_available_ovens);
  check_return_code(rc, 'u');

  //Order is assigned to a deliverer

  //Calculate time for an order to be delivered
  int waiting_delivery = (rand_r(&seed) % (max_waiting - min_waiting +1))+min_waiting;
  sleep(waiting_delivery);

  clock_gettime(CLOCK_REALTIME, &order_delivered);

  //time for a deliverer to come back
  sleep(waiting_delivery);


  //Release deliverer
  rc = pthread_mutex_lock(&mutex_available_deliverer);
  check_return_code(rc, 'l');

  ++available_deliverer;
  pthread_cond_signal(&cond_available_deliverer);
  rc = pthread_mutex_unlock(&mutex_available_deliverer);
  check_return_code(rc, 'u');


  //Calculate time needed
  waiting_duration = prep_start.tv_sec - order_start.tv_sec;
  prep_duration = bake_start.tv_sec - prep_start.tv_sec;
  bake_duration = order_done.tv_sec - bake_start.tv_sec;
  delivery_duration = order_delivered.tv_sec - order_done.tv_sec;

  time_X = waiting_duration + prep_duration + bake_duration + delivery_duration;
  time_Y = delivery_duration;



  //Print results
  rc = pthread_mutex_lock(&mutex_console);
  check_return_code(rc, 'l');
  printf("Order with id:%d has been delivered in %f seconds and cooled off for %f seconds\n",*cid, time_X, time_Y );
  rc = pthread_mutex_unlock(&mutex_console);
  check_return_code(rc, 'u');

  //Update global variables
  //Average
  rc = pthread_mutex_lock(&mutex_avg);
  check_return_code(rc, 'l');
  sum_X = sum_X + time_X;
  sum_Y = sum_Y + time_Y;
  rc = pthread_mutex_unlock(&mutex_avg);
  check_return_code(rc, 'u');
  //Max

  max_delivery[*cid] = time_X;
  max_cool[*cid] = time_Y;



  pthread_exit(NULL);
}


int main(int argc, char *argv[])
{
  //Checking for the appropriate number of argumnets
  if (argc != 3) {
		printf("Error: The program must recieve 2 arguments: number of customers and seed)\n\n\n");
		exit(-1);
	}

  //Getting the number of customers ans seed
	int customers = atoi(argv[1]);
   	if (customers <= 0) {
		printf("Error: The number of customers should be a positive integer\n");
		exit(-1);
	}
  double *orders = malloc(customers*sizeof(double));
  if (orders == NULL) {
    printf("NOT ENOUGH MEMORY!\n");
    return -1;
  }
  seed = atoi(argv[2]);


  orders = malloc(sizeof(double)*customers);
	for(int i=0;i<customers;++i)
	{
		orders[i] = -1;
	}

  available_cooks = cooks;
  available_ovens = ovens;
  available_deliverer = deliverer;

  //Initializing arrays
	max_delivery = malloc(sizeof(double)*customers);
	for(int i=0;i<customers;++i)
	{
		max_delivery[i] = -1;
	}
  max_cool = malloc(sizeof(double)*customers);
	for(int i=0;i<customers;++i)
	{
		max_cool[i] = -1;
	}



  printf("\n \nWelcome to AUEB's Pizza Store simulation.\nYou have chosen %d for the number of customers and seed: %d \n \n \n", customers, seed);


  pthread_t *threads;
  threads = malloc(customers * sizeof(pthread_t));
  if (threads == NULL) {
		printf("NOT ENOUGH MEMORY!\n");
		return -1;
	}

  /**
  *Creating threads
  **/
  int rc;
  int threadCount;
  int countArray[customers];
  for(threadCount = 0; threadCount < customers; threadCount++) {
    //printf("Main: creating thread %d\n", threadCount);
    countArray[threadCount] = threadCount + 1;
    rc = pthread_create(&threads[threadCount], NULL, execute, &countArray[threadCount]);
    //Check if thread was created correctly
    if (rc != 0) {
      printf("ERROR: return code from pthread_create() is %d\n", rc);
      exit(-1);
    }
  }

  /**
  *Waiting for threads to finish
  **/
  void *status;
  for (threadCount = 0; threadCount < customers; threadCount++) {
    rc = pthread_join(threads[threadCount], &status);

    if (rc != 0) {
      printf("ERROR: return code from pthread_join() is %d\n", rc);
      exit(-1);
    }
    //double dbl = *(double*) status;
    //printf("Main: Thread %d returned %f as status code.\n", countArray[threadCount], dbl);

    //orders[countArray[threadCount]] = dbl;

  }


  double avg_time_for_delivery = sum_X/customers;
  double avg_time_to_cool = sum_Y/customers;
  printf("Average time for an order to be delivered: %f\n",avg_time_for_delivery );
  printf("Average time for an order to cool: %f\n",avg_time_to_cool );


  double max_d = -1;
  double max_c = -1;
  for(int i=0; i<=customers; ++i){
    if(max_d <= max_delivery[i]){
      max_d = max_delivery[i];
    }
    if(max_c <= max_cool[i]){
      max_c = max_cool[i];
    }
  }
printf("Maximum delivery duration was: %f\n",max_d );
printf("Maximum cooling duration of an order was: %f\n",max_c );



  free(threads);
  return 1;

}

void check_return_code(int return_code, char r)
{
	if (return_code != 0)
  {
    if(r == 'u'){ //unlock error
      printf("ERROR: return code from pthread_mutex_unlock() is %d\n", return_code);
      pthread_exit(&return_code);
    }else{ //lock error
      printf("ERROR: return code from pthread_mutex_lock() is %d\n", return_code);
      pthread_exit(&return_code);
    }
	}
}
