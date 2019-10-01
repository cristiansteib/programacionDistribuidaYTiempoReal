/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#ifndef _FILESYSTEM_H_RPCGEN
#define _FILESYSTEM_H_RPCGEN

#include <rpc/rpc.h>


#ifdef __cplusplus
extern "C" {
#endif

#define BUFFER_SIZE 255
#define FILE_NAME_SIZE 100

struct response_H {
	u_char data[BUFFER_SIZE];
	int bytes_readed;
};
typedef struct response_H response_H;

struct read_H {
	char *fileName;
	int offset;
	int count;
};
typedef struct read_H read_H;

struct write_H {
	u_char data[BUFFER_SIZE];
	char *fileName;
	int count;
};
typedef struct write_H write_H;

#define filesystem_prg 0x20000001
#define filesystem_ver 1

#if defined(__STDC__) || defined(__cplusplus)
#define readFile 1
extern  struct response_H * readfile_1(struct read_H *, CLIENT *);
extern  struct response_H * readfile_1_svc(struct read_H *, struct svc_req *);
#define writeFile 2
extern  int * writefile_1(struct write_H *, CLIENT *);
extern  int * writefile_1_svc(struct write_H *, struct svc_req *);
extern int filesystem_prg_1_freeresult (SVCXPRT *, xdrproc_t, caddr_t);

#else /* K&R C */
#define readFile 1
extern  struct response_H * readfile_1();
extern  struct response_H * readfile_1_svc();
#define writeFile 2
extern  int * writefile_1();
extern  int * writefile_1_svc();
extern int filesystem_prg_1_freeresult ();
#endif /* K&R C */

/* the xdr functions */

#if defined(__STDC__) || defined(__cplusplus)
extern  bool_t xdr_response_H (XDR *, response_H*);
extern  bool_t xdr_read_H (XDR *, read_H*);
extern  bool_t xdr_write_H (XDR *, write_H*);

#else /* K&R C */
extern bool_t xdr_response_H ();
extern bool_t xdr_read_H ();
extern bool_t xdr_write_H ();

#endif /* K&R C */

#ifdef __cplusplus
}
#endif

#endif /* !_FILESYSTEM_H_RPCGEN */
