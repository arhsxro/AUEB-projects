#include "p3160153-p3160194-p3140290-pizza1.h"

void check_return_code(int return_code, char r);


pthread_mutex_t mutex_available_cooks;
pthread_mutex_t mutex_available_ovens;
pthread_mutex_t mutex_count;
pthread_mutex_t mutex_console;
pthread_mutex_t mutex_total_order_completion;

pthread_cond_t cond_available_cooks;
pthread_cond_t cond_available_ovens;

int available_cooks;
int available_ovens;
unsigned int seed;
int count=-1;
double *orders;

void * execute(void * customer_id)
{
  int *cid;
  cid = (int *)customer_id;
  int rc;
  struct timespec order_start;
  struct timespec prep_start;
  struct timespec bake_start;
  struct timespec order_done;
  double waiting_duration;
  double prep_duration;
  double bake_duration;
  double local_tot;
  double* total = (double*)malloc(sizeof(double));

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

  //printf("Order with id:%d is accepted.\n",*cid );
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
  //printf("Order with id:%d has %d number of pizzas!!\n",*cid, number_of_pizzas );
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


  //All pizzas of the same order are baking in the same oven for T_bake
  sleep(T_bake);

  //Release oven
  rc = pthread_mutex_lock(&mutex_available_ovens);
  check_return_code(rc, 'l');

	++available_ovens;

	pthread_cond_signal(&cond_available_ovens);
	rc = pthread_mutex_unlock(&mutex_available_ovens);
  check_return_code(rc, 'u');


  //Order is ready


  //Release cook
  rc = pthread_mutex_lock(&mutex_available_cooks);
  check_return_code(rc, 'l');

	++available_cooks;

  clock_gettime(CLOCK_REALTIME, &order_done); //4: track time when  order is ready and handed to the customer
	pthread_cond_signal(&cond_available_cooks);
	rc = pthread_mutex_unlock(&mutex_available_cooks);
  check_return_code(rc, 'u');


  //calculate time needed
  waiting_duration = prep_start.tv_sec - order_start.tv_sec;
  prep_duration = bake_start.tv_sec - prep_start.tv_sec;
  bake_duration = order_done.tv_sec - bake_start.tv_sec;
  local_tot = waiting_duration + prep_duration + bake_duration;
  *total = waiting_duration + prep_duration + bake_duration;


  //Print results
  rc = pthread_mutex_lock(&mutex_console);
  check_return_code(rc, 'l');
  printf("Order with id:%d has completed in %f seconds\n",*cid, local_tot );
  rc = pthread_mutex_unlock(&mutex_console);
  check_return_code(rc, 'u');


  return total;
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
    double dbl = *(double*) status;
    //printf("Main: Thread %d returned %f as status code.\n", countArray[threadCount], dbl);

    orders[countArray[threadCount]] = dbl;

  }

  double max;
  double sum;
  for(int i=0; i<customers; i++){
    //printf("Order with id:%d was prepared in %f seconds.\n",i+1, orders[i+1] );
    sum += orders[i];
    if(max < orders[i])
			max = orders[i];
  }
  printf("Maximum time for an order to be prepared: %f \n", max);
  printf("Average time for an order to be prepared: %f \n", sum /customers);


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
