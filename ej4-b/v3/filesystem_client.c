/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "filesystem.h"


void
filesystem_prg_1(char *host)
{
	CLIENT *clnt;
	struct response_H  *result_1;
	struct read_H  readfile_1_arg;
	int  *result_2;
	struct write_H  writefile_1_arg;

#ifndef	DEBUG
	clnt = clnt_create (host, filesystem_prg, filesystem_ver, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */

	result_1 = readfile_1(&readfile_1_arg, clnt);
	if (result_1 == (struct response_H *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_2 = writefile_1(&writefile_1_arg, clnt);
	if (result_2 == (int *) NULL) {
		clnt_perror (clnt, "call failed");
	}
#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	 /* DEBUG */
}


int
main (int argc, char *argv[])
{
	char *host;

	if (argc < 2) {
		printf ("usage: %s server_host\n", argv[0]);
		exit (1);
	}
	host = argv[1];
	filesystem_prg_1 (host);
exit (0);
}