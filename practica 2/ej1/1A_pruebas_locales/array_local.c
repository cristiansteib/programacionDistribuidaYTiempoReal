/* RPC client for addition of variable length array */

#include <stdio.h>
#include <stdlib.h>

/* Wrapper function takes care of calling the RPC procedure */

int vadd(int *array, int arrayLenght) {

  int result=0;
  
  printf("Got request: adding %d numbers\n",
         arrayLenght);

  for (int i=0; i < arrayLenght; i++){
    result += array[i];
  }
  return (result);
}


int main( int argc, char *argv[]) {

  int *ints,n;
  int i;
  int res;
  if (argc<3) {
    fprintf(stderr,"Usage: %s num1 num2 ...\n",argv[0]);
    exit(0);
  }

  /* get the 2 numbers that should be added */
  n = argc-1;
  ints = (int *) malloc(n * sizeof( int ));
  if (ints==NULL) {
    fprintf(stderr,"Error allocating memory\n");
    exit(0);
  }
  for (i=1;i<argc;i++) {
    ints[i-1] = atoi(argv[i]);
  }

  res = vadd(ints,n);
  printf("%d",ints[0]);
  for (i=1;i<n;i++) 
    printf(" + %d",ints[i]);
  printf(" = %d\n",res);
  return(0);
}


