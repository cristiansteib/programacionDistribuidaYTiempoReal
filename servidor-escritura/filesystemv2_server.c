/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "filesystemv2.h"

char **
lectura_1_svc(variablesLectura *argp, struct svc_req *rqstp)
{
	static char * result;
	char * aux;
	FILE *archivo;
	archivo = fopen(argp->nombreA,"rb");
	// Con respecto al buffer, fijarse en el tamaño,se tiene que armar alguna estructura para que el argp->cantLeerA sea igual al tamaño del 	char *buffer....y ver la parte de trasmitir por partes para archivo grandes.
	printf("Nombre del archivo a leer: %s\n",argp->nombreA );	
	if(archivo == NULL){
		printf("El archivo solicitado no existe.\n");
		result = NULL;
	}else{
		printf("El archivo se abrio correctamente\n");	
		//posicionamiento del puntero
		fseek(archivo,argp->posicionA, SEEK_SET); // ARCHIVO, POSICION, FORMATO DE SET
		printf("Posicion buscada en el archivo: %d\n",argp->posicionA );
		
		//lectura del archivo
		aux = (char *)calloc(argp->cantLeerA, sizeof(unsigned char));
		fread(aux,sizeof(unsigned char),argp->cantLeerA,archivo);
		//printf("%s \n",aux);
		result = aux;
	//printf("%s \n",result);
		
		int resultado_posicion = ftell(archivo);
		
		resultado_posicion = resultado_posicion - argp->posicionA;
		printf("Strlen(result) = %d\n", strlen(result));
		printf("Posicion final = %d\n", resultado_posicion);
		fclose(archivo);
		printf("*******************************************************************\n");
		free(aux);

	}

	return &result;
}

int *
escritura_1_svc(variablesEscritura *argp, struct svc_req *rqstp)
{
	static int  result;

	char c;
	FILE *archivo, *archivo_remoto;
	archivo = fopen(argp->nombreA,"a");
	archivo_remoto = fopen(strcat(argp->nombreA, "_remoto"),"a");
	if(archivo == NULL || archivo_remoto == NULL){
		printf("no existe el archivo\n");
		result = -1; // Dado que no existe el archivo, se retorna -1 para indicar un error
	}else{
		printf("Nombre del archivo Escritura o creado: %s\n",argp->nombreA);
		
		int pos_inicial = ftell(archivo);	
		//Escritura de algo con salto de linea al final, sino la proxima escritura se hace a continuación
		fwrite(argp->bufferEscritura,sizeof(unsigned char),strnlen(argp->bufferEscritura,255),archivo);
		fwrite("\n",sizeof(unsigned char),1,archivo);
	
		//Copia en el archivo remoto
		fwrite(argp->bufferEscritura,sizeof(unsigned char),strnlen(argp->bufferEscritura,255),archivo_remoto);
		fwrite("\n",sizeof(unsigned char),1,archivo_remoto);

		printf("Lo que se copio al final del archivo:'%s'\n",argp->bufferEscritura);
		int pos_final = ftell(archivo);
		result = pos_final - pos_inicial;
		fclose(archivo);
		fclose(archivo_remoto);
		printf("*******************************************************************\n");
	}
	return &result;
}
