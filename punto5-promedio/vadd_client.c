/* RPC client for addition of variable length array */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include "vadd.h"

#define CANT_RPC 10
  /* Created for us by rpcgen - has everything we need ! */
/* Wrapper function takes care of calling the RPC procedure */

int vadd( CLIENT *clnt, int *x, int n, struct timeval *tv) {
  iarray arr;
  int *result;
  double tiempo;

  arr.iarray_len = n;
  arr.iarray_val = x;

  clnt_control(clnt, CLSET_TIMEOUT, tv);

  result = vadd_1(&arr, clnt);
  if (result==NULL) {
    clnt_perror (clnt, "call failed");
  }

  return(*result);
}


int main( int argc, char *argv[]) {


  struct timeval timeout;
  timeout.tv_sec = 5;
  timeout.tv_usec = 0;
  double parteDecimal, parteEntera;
  struct timespec start, stop;
  double tiempos[CANT_RPC];
  double prom=0;
  CLIENT *clnt;
  int *ints,n;
  int i;
  int res;
  if (argc<3) {
    fprintf(stderr,"Usage: %s hostname num1 num2 ...\n",argv[0]);
    exit(0);
  }
 
  clnt = clnt_create(argv[1], VADD_PROG, VADD_VERSION, "tcp");

  /* Make sure the create worked */
  if (clnt == (CLIENT *) NULL) {
    clnt_pcreateerror(argv[1]);
    exit(1);
  }

  /* get the 2 numbers that should be added */
  n = argc-2;
  ints = (int *) malloc(n * sizeof( int ));
  if (ints==NULL) {
    fprintf(stderr,"Error allocating memory\n");
    exit(0);
  }
  for (i=2;i<argc;i++) {
    ints[i-2] = atoi(argv[i]);
  }

  for (int cant=0; cant < CANT_RPC; cant++){
    clock_gettime( CLOCK_REALTIME, &start);
    res = vadd(clnt,ints,n,&timeout);
    clock_gettime( CLOCK_REALTIME, &stop);
    tiempos[cant] = ((stop.tv_sec - start.tv_sec ) * 1000 * 1000 * 1000) + (stop.tv_nsec - start.tv_nsec );
  }

  for (int i=0; i<CANT_RPC; i++){
    printf("[%d] %f\n",i,tiempos[i]);
    prom = prom + (tiempos[i]);
  }
  
  prom = prom / CANT_RPC;
  printf("Promedio de los 10 RPC: %.2f ns \n", prom );
  printf("Resultado de la suma: %d\n", res);


  printf("---------------------------------------------------\n");
  printf("---------Probando con timeout menor --------------\n");
  printf("---------------------------------------------------\n");

  double tt = (prom*0.9) / 1000000000 ;


  parteDecimal = modf(tt, &parteEntera);
  timeout.tv_usec = parteDecimal*1000000;
  timeout.tv_sec = parteEntera;

  printf("Cambiando el timeout a: %ld sec %ld uS \n", timeout.tv_sec,timeout.tv_usec );

  for (int cant=0; cant < CANT_RPC; cant++){
    res = vadd(clnt,ints,n, &timeout);
    printf("[%d] %s\n",cant, (res==NULL)? "FAIL": "OK");
  }

  return(0);
}



