/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "vadd.h"

bool_t
xdr_iarray (XDR *xdrs, iarray *objp)
{
	register int32_t *buf;

	 if (!xdr_array (xdrs, (char **)&objp->iarray_val, (u_int *) &objp->iarray_len, ~0,
		sizeof (int), (xdrproc_t) xdr_int))
		 return FALSE;
	return TRUE;
}