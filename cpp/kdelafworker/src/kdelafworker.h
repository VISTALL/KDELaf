

#ifndef _KDELAFWORKER_H_
#define _KDELAFWORKER_H_

#ifdef HAVE_CONFIG_H
#include <config.h>
#endif

#ifdef HAVE_CONFIG_H
#include <config.h>
#endif

#include <kapplication.h>
#include <kmainwindow.h>
#include <qtimer.h>
#include <kstyle.h>
#include <qpixmap.h>
#include "qtooltiphack.h"
#include "baseio.h"

/**
 * @short Application Main Window
 * @author Sekou DIAKITE <diakite@freeasinspeech.org>
 * @version alpha
 */
class kdelafworker : public KMainWindow
{
    Q_OBJECT
public:
    /**
     * Default Constructor
     */
    kdelafworker(KApplication *app);

    /**
     * Default Destructor
     */
    virtual ~kdelafworker();

    bool sendBaseDefaults();
    bool getLocalePaths();
    bool getMimePaths();
    bool getColorByKey();
    bool getButtonImage();
    bool getPixelMetric();
    bool getTabImage();
    bool getMenuItemImage(bool isCheckBox = false, bool isRadioButton = false);
    bool getCheckBoxMenuItemImage();
    bool getRadioButtonMenuItemImage();
    bool getMenuImage();
    bool getMenuBarImage();
    bool getSimplePrefferedSize();
    bool getToolTipDefaults();
    bool getTextFieldDefaults();
    bool getLabelDefaults();
    bool getComboBoxDefaults();
    bool getTreeDefaults();
    bool getToolbarButtonImage();
    bool getToolBarSeparatorImage();
    bool getToolbarBackgroundImage();
    bool getToolbarHandleImage();
    bool getComboBoxImage();
    bool getIconPaths();
    bool getConfigPaths();
    bool getComboBoxSize();
    bool getComboValueRect();
    bool getIndicatorSize(bool exclusive);
    bool getIndicatorImage(bool exclusive);
    bool getMenuButtonIndicatorImage();
    bool getScrollBarThumbImage();
    bool getScrollBarTrackImage();
    bool getScrollBarMetrics();
    bool getScrollBarNextImage();
    bool getScrollBarPrevImage();
    bool getSpinnerImage();
    bool getCommonImage();
    bool getTitleButtonImage();
    bool getTableHeaderImage();
    bool getProgressBarImage();
    bool getSliderHandleImage();
    bool getSliderGrooveImage();
    bool getThumbTrackOffset();
    bool getScrollBarImage();
    bool getButtonPreferredSize();
    bool getToolbarButtonPreferredSize();

protected:
    int JPMtoQPM(int JPM);
    QStyle::SubControl JSCtoQSC(int JSC);
    QPixmap *getToolbarBackground(int width, int height, bool horizontal, bool full = false);
    KStyle *kStyle();

protected:
    KApplication *app;
    bool isLittleEndian;
    KStyle kStyleFallback;
    OutBuffer *out;
    InBuffer *in;

public slots:
    void serverLoop();
};

#endif // _KDELAFWORKER_H_
