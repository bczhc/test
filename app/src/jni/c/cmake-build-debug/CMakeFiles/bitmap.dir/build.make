# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.15

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /home/zhc/bin/clion-2019.3.2/bin/cmake/linux/bin/cmake

# The command to remove a file.
RM = /home/zhc/bin/clion-2019.3.2/bin/cmake/linux/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/zhc/code/code/Android/some-tools/app/src/jni/c

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/zhc/code/code/Android/some-tools/app/src/jni/c/cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/bitmap.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/bitmap.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/bitmap.dir/flags.make

CMakeFiles/bitmap.dir/floatingboard/bitmap.c.o: CMakeFiles/bitmap.dir/flags.make
CMakeFiles/bitmap.dir/floatingboard/bitmap.c.o: ../floatingboard/bitmap.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/zhc/code/code/Android/some-tools/app/src/jni/c/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/bitmap.dir/floatingboard/bitmap.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/bitmap.dir/floatingboard/bitmap.c.o   -c /home/zhc/code/code/Android/some-tools/app/src/jni/c/floatingboard/bitmap.c

CMakeFiles/bitmap.dir/floatingboard/bitmap.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/bitmap.dir/floatingboard/bitmap.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/zhc/code/code/Android/some-tools/app/src/jni/c/floatingboard/bitmap.c > CMakeFiles/bitmap.dir/floatingboard/bitmap.c.i

CMakeFiles/bitmap.dir/floatingboard/bitmap.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/bitmap.dir/floatingboard/bitmap.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/zhc/code/code/Android/some-tools/app/src/jni/c/floatingboard/bitmap.c -o CMakeFiles/bitmap.dir/floatingboard/bitmap.c.s

# Object files for target bitmap
bitmap_OBJECTS = \
"CMakeFiles/bitmap.dir/floatingboard/bitmap.c.o"

# External object files for target bitmap
bitmap_EXTERNAL_OBJECTS =

bitmap: CMakeFiles/bitmap.dir/floatingboard/bitmap.c.o
bitmap: CMakeFiles/bitmap.dir/build.make
bitmap: CMakeFiles/bitmap.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/zhc/code/code/Android/some-tools/app/src/jni/c/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking C executable bitmap"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/bitmap.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/bitmap.dir/build: bitmap

.PHONY : CMakeFiles/bitmap.dir/build

CMakeFiles/bitmap.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/bitmap.dir/cmake_clean.cmake
.PHONY : CMakeFiles/bitmap.dir/clean

CMakeFiles/bitmap.dir/depend:
	cd /home/zhc/code/code/Android/some-tools/app/src/jni/c/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/zhc/code/code/Android/some-tools/app/src/jni/c /home/zhc/code/code/Android/some-tools/app/src/jni/c /home/zhc/code/code/Android/some-tools/app/src/jni/c/cmake-build-debug /home/zhc/code/code/Android/some-tools/app/src/jni/c/cmake-build-debug /home/zhc/code/code/Android/some-tools/app/src/jni/c/cmake-build-debug/CMakeFiles/bitmap.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/bitmap.dir/depend
