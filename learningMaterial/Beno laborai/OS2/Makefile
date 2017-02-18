CC = gcc
CFLAGS = -I.
DEPS_OS = glo.h #OS header files

all: $(DEPS_OS)
	$(CC) vm.h rm.h glo.h os.c vm.c rm.c my_lib.c -o OS  $(CFLAGS)

hdd:
	$(CC) -o hdd_new hdd_new.c $(CFLAGS)\
	./hdd_new  #Execute it (create new HDD image)
