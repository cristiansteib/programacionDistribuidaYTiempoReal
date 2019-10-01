const BUFFER_SIZE = 255;
const FILE_NAME_SIZE = 100;

struct response_H {
	unsigned char data[BUFFER_SIZE];  	// Data readed
	int bytes_readed;					// Effective count bytes readed
};

struct read_H {
	string fileName<FILE_NAME_SIZE>; 
	int offset;
	int count;
};

struct write_H {
	unsigned char data[BUFFER_SIZE]; 
	string fileName<FILE_NAME_SIZE>; 
	int count;
};

program filesystem_prg {
	version filesystem_ver{
		struct response_H readFile(struct read_H vl)=1;
		int writeFile(struct write_H ve)=2;
	}=1;
}=0x20000001;
