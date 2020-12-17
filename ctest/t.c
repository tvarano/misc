#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

int main() {
	pid_t pid; 
	pid = fork();

	if (pid > 0) {
		wait(NULL);
		printf("W\n");
		pid = fork();
		printf("X\n");
	} else {
		pid = fork();
		if (pid > 0)
			printf("Y\n");
		else printf("Z\n");
	}
	return 0;

}

