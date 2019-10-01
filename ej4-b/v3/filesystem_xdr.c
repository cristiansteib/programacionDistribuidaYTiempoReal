/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "filesystem.h"

bool_t
xdr_response_H (XDR *xdrs, response_H *objp)
{
	register int32_t *buf;

	int i;
	 if (!xdr_vector (xdrs, (char *)objp->data, BUFFER_SIZE,
		sizeof (u_char), (xdrproc_t) xdr_u_char))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->bytes_readed))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_read_H (XDR *xdrs, read_H *objp)
{
	register int32_t *buf;

	 if (!xdr_string (xdrs, &objp->fileName, FILE_NAME_SIZE))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->offset))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->count))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_write_H (XDR *xdrs, write_H *objp)
{
	register int32_t *buf;

	int i;
	 if (!xdr_vector (xdrs, (char *)objp->data, BUFFER_SIZE,
		sizeof (u_char), (xdrproc_t) xdr_u_char))
		 return FALSE;
	 if (!xdr_string (xdrs, &objp->fileName, FILE_NAME_SIZE))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->count))
		 return FALSE;
	return TRUE;
}
