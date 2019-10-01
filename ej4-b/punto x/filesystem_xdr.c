/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "filesystem.h"

bool_t
xdr_response (XDR *xdrs, response *objp)
{
	register int32_t *buf;

	int i;
	 if (!xdr_vector (xdrs, (char *)objp->bytesReaded, BUFFER_SIZE,
		sizeof (u_char), (xdrproc_t) xdr_u_char))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->totalBytes))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_readHeaders (XDR *xdrs, readHeaders *objp)
{
	register int32_t *buf;

	 if (!xdr_string (xdrs, &objp->fileName, FILE_NAME_SIZE))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->position))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->totalBytes))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_writeHeaders (XDR *xdrs, writeHeaders *objp)
{
	register int32_t *buf;

	int i;
	 if (!xdr_string (xdrs, &objp->fileName, FILE_NAME_SIZE))
		 return FALSE;
	 if (!xdr_vector (xdrs, (char *)objp->buffer, BUFFER_SIZE,
		sizeof (u_char), (xdrproc_t) xdr_u_char))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->totalBytes))
		 return FALSE;
	return TRUE;
}
