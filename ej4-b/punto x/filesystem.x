const BUFFER_SIZE = 255;
const FILE_NAME_SIZE = 100;

struct response {
	unsigned char bytesReaded[BUFFER_SIZE]; 	/* Los bytes leidos */
	int totalBytes;					/* La cantidad que se pudo leer */
};

struct readHeaders {
	string fileName<FILE_NAME_SIZE>; 
	int position;
	int totalBytes;
};

struct writeHeaders {
	string fileName<FILE_NAME_SIZE>; 
	unsigned char buffer[BUFFER_SIZE]; 
	int totalBytes;
};

program filesystem_prg {
	version filesystem_ver{
		struct response readFile(struct readHeaders vl)=1;
		int writeFile(struct writeHeaders ve)=2;
	}=1;
}=0x20000001;
