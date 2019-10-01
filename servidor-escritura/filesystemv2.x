#define VERSION_NUMBER 1

/* If I want to put something explicitly in the .h file produced by rpcgen
   I can start it with %:
*/

#define foo 127
/*
Contiene la estructura de lo que se envia en el momento
de conexion
*/
struct variablesLectura{

  string nombreA<255>;
  int posicionA;
  int cantLeerA;
 
};

struct variablesEscritura{
	string nombreA<255>;
	int cantBytes;
    string bufferEscritura<255>;
};

program SERVIDORCF_PROG {
  version SIMP_VERSION {
    string LECTURA(variablesLectura) = 1;
    int ESCRITURA(variablesEscritura) = 2;
  } = VERSION_NUMBER;

} = 0x20000001;
