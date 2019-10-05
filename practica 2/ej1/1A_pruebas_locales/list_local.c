
#include <stdio.h>
#include <stdlib.h>

/*Struct for linked list extracted from ll.h file*/
struct foo {
  int x;
  struct foo *next;
};
typedef struct foo foo;


void printnums( foo *f) {
  
  while (f) {
    printf("%d ",f->x);
    f=f->next;
  }
  printf("\n");
}

void print_sum( foo *argp) {
  int result=0;

  while (argp) {
    result += argp->x;
    argp = argp->next;
  }
  printf("Sum is %d\n",result); 
}



int main( int argc, char *argv[]) {
  int n,i;
  foo *f;
  foo *head;
  foo *prev;

  if (argc<3) {
    fprintf(stderr,"Usage: %s num1 num2 ...\n",argv[0]);
    exit(0);
  }


  /* Create a CLIENT data structure that reference the RPC
     procedure SIMP_PROG, version SIMP_VERSION running on the
     host specified by the 1st command line arg. */

  //clnt = clnt_create(argv[1], LL_PROG, LL_VERSION, "udp"); 

  /* Make sure the create worked
  if (clnt == (CLIENT *) NULL) {
    clnt_pcreateerror(argv[1]);
    exit(1);
  }
*/
  n = argc-1;
  f = head = (foo *) malloc(sizeof(foo));
  for (i=0;i<n;i++) {
    f->x = atoi(argv[i+1]);
    f->next = (foo *) malloc(sizeof(foo));
    prev=f;
    f = f->next;
  }

  free(prev->next);
  prev->next=NULL;

  printnums(head);
  print_sum(head);
  return(0);
}



