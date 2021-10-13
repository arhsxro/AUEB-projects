#include <stdio.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdlib.h>

//Constants
const int cooks = 6;

const int ovens = 5;

const long order_low = 1; //time that an order takes place
const long order_high = 5;
const int min_order = 1;  //pizzas that can be ordered in a single order
const int max_order = 5;

const long T_prep = 1;
const long T_bake = 10;
