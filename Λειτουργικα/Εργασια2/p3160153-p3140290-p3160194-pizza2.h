#include <stdio.h>
#include <pthread.h>
#include <stdbool.h>
#include <stdlib.h>

const int cooks = 2;
const int ovens = 5;
const int deliverer = 10;

const long order_low = 1;
const long order_high = 5;

const int min_order = 1;
const int max_order = 5;

const long T_prep = 1;
const long T_bake = 10;

const long min_waiting = 5;
const long max_waiting = 10;
