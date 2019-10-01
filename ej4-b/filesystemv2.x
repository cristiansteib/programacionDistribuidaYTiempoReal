const MAX_STRUCT=255;
const MAX_FILE=100;

struct retorno{
	unsigned char ret_bytes[MAX_STRUCT]; 		/* Los bytes leidos */
	int cant_bytes;						/* La cantidad que se pidio leer */  /***PREGUNTAR****/
	int bytes_leidos;					/* La cantidad que se pudo enviar */
};

struct valoresLectura {
	string nombreA<MAX_FILE>; 
	int pos;
	int cant;
};

struct valoresEscritura {
	string nombreA<MAX_FILE>; 
	unsigned char buffer[MAX_STRUCT]; 
	int cant;	
};

program filesystem_prg{
	version filesystem_ver{
		struct retorno lectura(struct valoresLectura vl)=1;
		int escritura(struct valoresEscritura ve)=2;
	}=1;
}=0x20000001;
