

#include "kdelafworker.h"
#include <kapplication.h>
#include <kaboutdata.h>
#include <kcmdlineargs.h>
#include <klocale.h>

static const char description[] =
    I18N_NOOP("KdeLAF Worker");

static const char version[] = "alpha";

static KCmdLineOptions options[] =
{
//    { "+[URL]", I18N_NOOP( "Document to open" ), 0 },
    KCmdLineLastOption
};

int main(int argc, char **argv)
{
    KAboutData about("kdelafworker", I18N_NOOP("kdelafworker"), version, description,
		     KAboutData::License_Custom, "(C) 2006 Sekou DIAKITE", 0, 0, "diakite@freeasinspeech.org");
    about.addAuthor( "Sekou DIAKITE", 0, "diakite@freeasinspeech.org" );
    KCmdLineArgs::init(argc, argv, &about);
    KCmdLineArgs::addCmdLineOptions( options );
    KApplication app;
    kdelafworker *mainWin = 0;

	// no session.. just start up normally
	KCmdLineArgs *args = KCmdLineArgs::parsedArgs();
	
	/// @todo do something with the command line args here
	
	mainWin = new kdelafworker(&app);
	app.setMainWidget( mainWin );
	//mainWin->show();
	
	args->clear();
    QTimer::singleShot(0, mainWin, SLOT(serverLoop()));

    // mainWin has WDestructiveClose flag by default, so it will delete itself.
    return app.exec();
}

