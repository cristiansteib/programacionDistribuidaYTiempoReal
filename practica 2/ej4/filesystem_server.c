#include "filesystem.h"

struct response_H * readfile_1_svc(struct read_H *argp, struct svc_req *rqstp)
{
	static struct response_H  result;
	FILE *file;
	result.bytes_readed = 0;

	file = fopen(argp->fileName, "rb+");

	if (file == NULL)
	{
		printf("Can't open the file %s\n", argp->fileName);
	} else {
		fseek(file, argp->offset, SEEK_SET);
		result.bytes_readed = fread(result.data, sizeof(unsigned char), argp->count, file);
	}
	fclose(file);
	return &result;
}

int *writefile_1_svc(struct write_H *argp, struct svc_req *rqstp)
{
	static int  result;
	FILE *file;

	file = fopen(argp->fileName, "ab");

	if (file == NULL)
	{
		printf("Can't open the file %s", argp->fileName);
	} else {
		printf("Writing file: %s...", argp->fileName);
		result = fwrite(argp->data, sizeof(unsigned char), argp->count, file);
	}
	fclose(file);

	return &result;
}
