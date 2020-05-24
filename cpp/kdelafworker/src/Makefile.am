# set the include path for X, qt and KDE
INCLUDES = $(all_includes)

# these are the headers for your project
noinst_HEADERS = kdelafworker.h baseio.h qtooltiphack.h typedef.h

# let automoc handle all of the meta source files (moc)
METASOURCES = AUTO

messages: rc.cpp
	$(EXTRACTRC) `find . -name \*.ui -o -name \*.rc` > rc.cpp
	$(XGETTEXT) *.cpp -o $(podir)/kdelafworker.pot


#########################################################################
# APPLICATION SECTION
#########################################################################
# this is the program that gets installed.  it's name is used for all
# of the other Makefile.am variables
bin_PROGRAMS = kdelafworker

# the application source, library search path, and link libraries
kdelafworker_SOURCES = main.cpp kdelafworker.cpp baseio.cpp qtooltiphack.cpp
kdelafworker_LDFLAGS = $(KDE_RPATH) $(all_libraries)
kdelafworker_LDADD = $(LIB_KDEUI)

# this is where the desktop file will go 
shelldesktopdir   = $(kde_appsdir)/Utilities

# this is where the shell's XML-GUI resource file goes
shellrcdir   = $(kde_datadir)/kdelafworker


