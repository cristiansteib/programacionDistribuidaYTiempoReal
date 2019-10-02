/* RPC client for addition of variable length array */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "vadd.h"  /* Created for us by rpcgen - has everything we need ! */
/* Wrapper function takes care of calling the RPC procedure */
#define BILLION 1000000000L

int vadd( CLIENT *clnt, int *len, int val, float timeout) {
  iarray arr;
  int *result;
  double tiempo;

  /* Set up the iarray to send to the server */
  arr.iarray_len = len;
  arr.iarray_val = val;


  
  //seteamos el timeout de RPC a 5 seg
  struct timeval tv;
  tv.tv_sec = timeout;
  tv.tv_usec = 0;
  clnt_control(clnt,CLSET_TIMEOUT, &tv);


  result = vadd_1(&arr,clnt);
  if (result==NULL) {
    clnt_perror (clnt, "call failed");
    exit(0);
  }
  return(*result);
}


int main( int argc, char *argv[]) {



  struct timespec start, stop;
  double tiempos[10];
  double prom=0;
  CLIENT *clnt;
  int *ints,n;
  int i;
  int res;
  if (argc<3) {
    fprintf(stderr,"Usage: %s hostname num1 num2 ...\n",argv[0]);
    exit(0);
  }
  printf("lalal\n");
  clnt = clnt_create(argv[1], VADD_PROG, VADD_VERSION, "udp");

  /* Make sure the create worked */
  if (clnt == (CLIENT *) NULL) {
    printf("clt failed\n");
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

  float timeout = 25; // le dejamos el timeout por defecto para el primer experimento
  printf("Iniciando experimento..");
  for (int cant=0; cant < 10; cant++){
    clock_gettime( CLOCK_REALTIME, &start);
    res = vadd(clnt,ints,n, timeout);
    clock_gettime( CLOCK_REALTIME, &stop);
    tiempos[cant] = (stop.tv_sec - start.tv_sec ) + (stop.tv_nsec - start.tv_nsec );
  }

  for (int i=0; i<10; i++){
    prom = prom + tiempos[i]/1000;
  }
  
  prom = prom / 10;
  printf("Promedio de los 10 RPC: %.2f \n", prom );
  printf("Resultado de la suma: %d\n",res);

  /*-------------------------------
  Se setea un 10% menos el timeout  del valor promedio
  y se prueba 10 veces mas, para comprobar como falla
  ---------------------------------*/
  timeout = prom * 0.9;
  printf("---------------------\n");
  printf("---------------------\n");
  printf("Configurando timout en %f\n", timeout);

  for (int i=0; i < 10; i++){
    res = vadd(clnt,ints,n, timeout);
    printf("[%d] %s",i, (res==NULL)? "Fallo": "Correcta");
  }
  return(0);
}



