#include "kdelafworker.h"
#include <iostream>
#include <qlabel.h>
#include <qcheckbox.h>
#include <kmainwindow.h>
#include <klocale.h>
#include <qpopupmenu.h>
#include <qpushbutton.h>
#include <qmenubar.h>
#include <qprogressbar.h>
#include <qradiobutton.h>
#include <qlineedit.h>
#include <qtoolbutton.h>
#include <qpainter.h>
#include <qstyle.h>
#include <qsize.h>
#include <qtabbar.h>
#include <kpushbutton.h>
#include <kglobal.h>
#include <kstandarddirs.h>
#include <qlabel.h>
#include <qspinbox.h>
#include <qstringlist.h>
#include <qlistview.h>
#include <qcombobox.h>
#include <kicontheme.h>
#include <kfileiconview.h>
#include <ktoolbarbutton.h>
#include <klistview.h>
#include <qworkspace.h>
#include <qscrollbar.h>
#include <qwmatrix.h>
#include <qslider.h>
#include <qheader.h>
#include <private/qtitlebar_p.h>

void dump(QRect &r) {
	std::cerr << "QRect[" << r.x() << "," << r.y() << "," << r.width() << "," << r.height() << "]";
}

kdelafworker::kdelafworker(KApplication *app)
    : KMainWindow( 0, "kdelafworker" )
{
    // set the shell's ui resource file
    //setXMLFile("kdelafworkerui.rc");

    //new QLabel( "Hello World", this, "hello label" );
	this->app = app;
	QImage tmp;
	isLittleEndian = (tmp.systemByteOrder() == QImage::LittleEndian);
	out = new OutBuffer(isLittleEndian);
	in = new InBuffer(isLittleEndian);
}

kdelafworker::~kdelafworker()
{
	if (out != NULL)
		delete out;
	if (in != NULL)
		delete in;
}

KStyle *kdelafworker::kStyle() {
	KStyle *_kStyle;
	if (style().inherits("KStyle"))
		_kStyle = dynamic_cast<KStyle*>(&style());
	else
		_kStyle = &kStyleFallback;
	return _kStyle;
}

/**
 * 
 */
void kdelafworker::serverLoop()
{

	//std::cerr << "i|kdelafworker::serverLoop " << std::endl; std::cerr.flush();
	int opCode;
	do {
		while (app->hasPendingEvents())
			app->processEvents();
		if (in->readInt(opCode)) {
			switch (opCode) {
				case OP_KILL:
					std::cerr << "i|kdelafworker::serverLoop kill" << std::endl; std::cerr.flush();
					close();
					return;
					break;
				case OP_GET_LOCALEPATHS:
					getLocalePaths();
					break;
				case OP_GET_MIMEPATHS:
					getMimePaths();
					break;
				case OP_GET_BASE_DEFAULTS:
					sendBaseDefaults();
					break;
				case OP_GET_COLOR_BY_KEY:
					getColorByKey();
					break;
				case OP_GET_BUTTON_IMAGE:
					getButtonImage();
					break;
				case OP_GET_PIXEL_METRIC:
					getPixelMetric();
					break;
				case OP_GET_TAB_IMAGE:
					getTabImage();
					break;
				case OP_GET_MENUITEM_IMAGE:
					getMenuItemImage();
					break;
				case OP_GET_CHECKBOXMENUITEM_IMAGE:
					getCheckBoxMenuItemImage();
					break;
				case OP_GET_RADIOBUTTONMENUITEM_IMAGE:
					getRadioButtonMenuItemImage();
					break;
				case OP_GET_MENU_IMAGE:
					getMenuImage();
					break;
				case OP_GET_MENUBAR_IMAGE:
					getMenuBarImage();
					break;
				case OP_GET_SIMPLEPREFERREDSIZE:
					getSimplePrefferedSize();
					break;
				case OP_GET_TOOLTIPDEFAULTS:
					getToolTipDefaults();
					break;
				case OP_GET_TEXTFIELDDEFAULTS:
					getTextFieldDefaults();
					break;
				case OP_GET_LABELDEFAULTS:
					getLabelDefaults();
					break;
				case OP_GET_TOGGLEBUTTON_IMAGE:
					getToolbarButtonImage();
					break;
				case OP_GET_TOOLBARSEPARATOR_IMAGE:
					getToolBarSeparatorImage();
					break;
				case OP_GET_TOOLBARBACKGROUND_IMAGE:
					getToolbarBackgroundImage();
					break;
				case OP_GET_TOOLBARHANDLE_IMAGE:
					getToolbarHandleImage();
					break;
				case OP_GET_TREEDEFAULTS:
					getTreeDefaults();
					break;
				case OP_GET_ICONPATHS:
					getIconPaths();
					break;
				case OP_GET_CONFIGPATHS:
					getConfigPaths();
					break;
				case OP_GET_COMBOBOX_DISPLAY_SIZE:
					getComboBoxSize();
					break;
				case OP_GET_COMBOBOX_IMAGE:
					getComboBoxImage();
					break;
				case OP_GET_COMBOBOX_VALUE_RECT:
					getComboValueRect();
					break;
				case OP_GET_EXCLUSIVE_INDICATOR_DIM:
					getIndicatorSize(true);
					break;
				case OP_GET_INDICATOR_DIM:
					getIndicatorSize(false);
					break;
				case OP_GET_RADIOBUTTON_IMAGE:
					getIndicatorImage(true);
					break;
				case OP_GET_CHECKBOX_IMAGE:
					getIndicatorImage(false);
					break;
				case OP_GET_COMBOBOXDEFAULTS:
					getComboBoxDefaults();
					break;
				case OP_GET_MENUBUTTONINDICATOR_IMAGE:
					getMenuButtonIndicatorImage();
					break;
				case OP_GET_SCROLLBARTHUMB_IMAGE:
					getScrollBarThumbImage();
					break;
				case OP_GET_SCROLLBARTRACK_IMAGE:
					getScrollBarTrackImage();
					break;
				case OP_GET_SCROLLBAR_METRIS:
					getScrollBarMetrics();
					break;
				case OP_GET_SCROLLBARNEXT_IMAGE:
					getScrollBarNextImage();
					break;
				case OP_GET_SCROLLBARPREV_IMAGE:
					getScrollBarPrevImage();
					break;
				case OP_GET_SPINNER_IMAGE:
					getSpinnerImage();
					break;
				case OP_GET_COMMON_IMAGE:
					getCommonImage();
					break;
				case OP_GET_TITLE_BUTTON:
					getTitleButtonImage();
					break;
				case OP_GET_TABLEHEADER_IMAGE:
					getTableHeaderImage();
					break;
				case OP_GET_PROGRESSBAR_IMAGE:
					getProgressBarImage();
					break;
				case OP_GET_SLIDERHANDLE_IMAGE:
					getSliderHandleImage();
					break;
				case OP_GET_SLIDERGROOVE_IMAGE:
					getSliderGrooveImage();
					break;
				case OP_GET_THUMBTRACKOFFSET:
					getThumbTrackOffset();
					break;
				case OP_GET_STYLE_NAME:
					out->writeString(style().className());
					break;
				case OP_GET_KDE_VERSION:
					out->writeString(QString(KDE_VERSION_STRING));
					break;
				case OP_GET_SCROLLBAR_IMAGE:
					getScrollBarImage();
					break;
				case OP_GET_BUTTON_PREF_SZ:
					getButtonPreferredSize();
					break;
				case OP_GET_TOOLBUTTON_PREF_SZ:
					getToolbarButtonPreferredSize();
					break;
				default:
					std::cerr << "e|kdelafworker::serverLoop unknow opCode : " << opCode << std::endl; std::cerr.flush();
					close();
					return;
			}
		}
	}
	while (true);
}

/*!
    \fn kdelafworker::sendBaseDefaults()
 */
bool kdelafworker::sendBaseDefaults()
{
	int widgetType = -1;
	bool ok = false;
	QWidget *widget = NULL;
	if (in->readInt(widgetType)) {
		ok = true;
		switch (widgetType) {
			case WT_BUTTON:
				widget = new KPushButton(this);
				break;
			case WT_CHECKBOXMENUITEM:
				widget = new QPopupMenu(this);
				break;
			case WT_CHECKBOX:
				widget = new QCheckBox(this);
				break;
			case WT_MENUBAR:
				widget = new QMenuBar(this);
				break;
			case WT_MENUITEM:
				widget = new QPopupMenu(this);
				break;
			case WT_MENU:
				widget = new QPopupMenu(this);
				break;
			case WT_POPUPMENU:
				widget = new QPopupMenu(this);
				break;
			case WT_PROGRESSBAR:
				widget = new QProgressBar(100, this);
				break;
			case WT_RADIOBUTTONMENUITEM:
				widget = new QPopupMenu(this);
				break;
			case WT_RADIOBUTTON:
				widget = new QRadioButton(this);
				break;
			case WT_TEXTFIELD:
				widget = new QLineEdit(this);
				break;
			case WT_TOGGLEBUTTON:
				widget = new KToolBarButton(this);
				break;
			case WT_TABBEDPANE:
				widget = new QTabBar(this);
				break;
			case WT_TOOLBAR:
				widget = new QToolBar(this);
				break;
			case WT_COMBOBOX:
				widget = new QComboBox(true, this);
				break;
			case WT_SPINNER:
				widget = new QSpinBox(this);
				break;
			default:
				std::cerr << "e|kdelafworker::sendBaseDefaults unknow widget : " << widgetType << std::endl; std::cerr.flush();
				ok = false;
		}
	}
	if (ok) {
		QColor background = widget->paletteBackgroundColor();
		QColor foreground = widget->paletteForegroundColor();
		QFont font = widget->font();

		out->writeColor(background);
		out->writeColor(foreground);
		out->writeFont(font);
		ok = out->send();
		if (widget != NULL)
			delete widget;
	}
	return ok;
}


bool kdelafworker::getSimplePrefferedSize()
{
	int key = -1;
	bool ok = false;
	QSize sz;
	if (in->readInt(key)) {
		ok = true;
		if (key == SZ_MENUBAR) {
			QMenuBar wrapper(this);
			wrapper.insertItem("hello");
			sz = wrapper.sizeHint();
			//delete wrapper;
		}
		else if (key == SZ_HPROGRESSBAR) {
			QProgressBar wrapper(100, this);
			wrapper.setProgress(50);
			wrapper.setCenterIndicator(false);
			wrapper.setPercentageVisible(false);
			sz = wrapper.sizeHint();
		}
		else if (key == SZ_VPROGRESSBAR) {
			QProgressBar wrapper(100, this);
			wrapper.setProgress(50);
			wrapper.setCenterIndicator(false);
			wrapper.setPercentageVisible(false);
			sz = wrapper.sizeHint();
			int width = sz.width();
			sz.setWidth(sz.height());
			sz.setHeight(width);
		}
		else if (	(key == SZ_HSCROLLBAR) ||
				(key == SZ_HSCROLLBARMIN) ||
				(key == SZ_VSCROLLBAR) ||
				(key == SZ_VSCROLLBARMIN)
					) {
			QScrollBar wrapper(this);
			wrapper.setMinValue(0);
			wrapper.setMaxValue(65535);
			wrapper.setValue(32767);
			wrapper.setPageStep(1);
			if (key == SZ_HSCROLLBAR) {
				wrapper.setOrientation(Qt::Horizontal);
				sz = wrapper.sizeHint();
			}
			else if (key == SZ_HSCROLLBARMIN) {
				wrapper.setOrientation(Qt::Horizontal);
				sz = wrapper.minimumSize();
			}
			else if (key == SZ_VSCROLLBAR) {
				wrapper.setOrientation(Qt::Vertical);
				sz = wrapper.sizeHint();
			}
			else if (key == SZ_VSCROLLBARMIN) {
				wrapper.setOrientation(Qt::Vertical);
				sz = wrapper.minimumSize();
			}
			else
				ok = false;
		}
		else
			ok = false;
		
	}
	if (ok) {
		out->writeInt(sz.width());
		out->writeInt(sz.height());
		ok = out->send();
	}
	else {
		out->writeInt(0);
		out->writeInt(0);
		out->send();
		std::cerr << "e|kdelafworker::getSimplePrefferedSize unknow key : " << key << std::endl;
		std::cerr.flush();
	}
	return ok;
}

/*!
    \fn kdelafworker::getColorByKey()
 */
bool kdelafworker::getColorByKey()
{
	int key = -1;
	bool ok = false;
	QColor *color = NULL;
	if (in->readInt(key)) {
		ok = true;
		switch (key) {
			case CR_DESKTOP:
				color = new QColor(palette().active().background());
				break;
			case CR_ACTIVECAPTION:
				color = new QColor(palette().active().background());
				break;
			case CR_ACTIVECAPTIONTEXT:
				color = new QColor(palette().active().foreground());
				break;
			case CR_ACTIVECAPTIONBORDER:
				color = new QColor(0, 0, 0);
				break;
			case CR_INACTIVECAPTION:
				color = new QColor(palette().inactive().background());
				break;
			case CR_INACTIVECAPTIONTEXT:
				color = new QColor(palette().inactive().foreground());
				break;
			case CR_INACTIVECAPTIONBORDER:
				color = new QColor(0, 0, 0);
				break;
			case CR_WINDOW:
				color = new QColor(palette().active().background());
				break;
			case CR_WINDOWBORDER:
				color = new QColor(0, 0, 0);
				break;
			case CR_WINDOWTEXT:
				color = new QColor(palette().active().foreground());
				break;
			case CR_MENU:
				color = new QColor(palette().active().background());
				break;
			case CR_MENUTEXT:
				color = new QColor(palette().active().foreground());
				break;
			case CR_TEXT:
				color = new QColor(palette().active().base());
				break;
			case CR_TEXTTEXT:
				color = new QColor(palette().active().text());
				break;
			case CR_TEXTHIGHLIGHT:
				color = new QColor(palette().active().highlight());
				break;
			case CR_TEXTHIGHLIGHTTEXT:
				color = new QColor(palette().active().highlightedText());
				break;
			case CR_TEXTINACTIVETEXT:
				color = new QColor(palette().inactive().text());
				break;
			case CR_CONTROL:
				color = new QColor(palette().active().background());
				break;
			case CR_CONTROLTEXT:
				color = new QColor(palette().active().foreground());
				break;
			case CR_CONTROLHIGHLIGHT:
				color = new QColor(palette().active().midlight());
				break;
			case CR_CONTROLLTHIGHLIGHT:
				color = new QColor(palette().active().light());
				break;
			case CR_CONTROLSHADOW:
				color = new QColor(palette().active().shadow());
				break;
			case CR_CONTROLDKSHADOW:
				color = new QColor(palette().active().dark());
				break;
			case CR_SCROLLBAR:
				color = new QColor(255, 0, 0);
				break;
			case CR_INFO:
				color = new QColor(255, 255, 100);
				break;
			case CR_INFOTEXT:
				color = new QColor(0, 0, 0);
				break;
			default:
				std::cerr << "e|kdelafworker::getColorByKey unknow key : " << key << std::endl;
				std::cerr.flush();
				color = new QColor(0, 0, 0);
		}
	}
	if (ok) {
		out->writeColor(*color);
		ok = out->send();
		delete color;
	}
	return ok;
}



/*!
    \fn kdelafworker::getButtonImage()
 */
bool kdelafworker::getButtonImage()
{
	int width, height;
	bool enabled, pressed, rollover, focus, contentAreaFilled, borderPainted;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(enabled);
	ok = ok && in->readBool(pressed);
	ok = ok && in->readBool(rollover);
	ok = ok && in->readBool(focus);
	ok = ok && in->readBool(contentAreaFilled);
	ok = ok && in->readBool(borderPainted);
	
	if (ok) {
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		KPushButton wrapper(this);
		wrapper.setText("");
		wrapper.setBackgroundOrigin(QWidget::ParentOrigin);
		wrapper.setGeometry(0, 0, width, height);

		int bStyle = QStyle::Style_Default;
		if (enabled)
			bStyle = QStyle::Style_Enabled;
		if (pressed)
			bStyle |= QStyle::Style_Down;
		if (rollover)
			bStyle |= QStyle::Style_MouseOver;
		if (focus)
			bStyle |= QStyle::Style_HasFocus;
		painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		if (contentAreaFilled) {
			style().drawControl(QStyle::CE_PushButton, &painter, &wrapper, QRect(0,0,width, height), palette().active(), bStyle);
			style().drawControl(QStyle::CE_PushButtonLabel, &painter, &wrapper, QRect(0,0,width, height), palette().active(), bStyle);
		} else if (borderPainted) {
			painter.setPen(wrapper.paletteForegroundColor());
			painter.drawRect(0, 0, width, height);
		}
		painter.end();
		out->writeImage(pixmap.convertToImage(), palette().active().background());
		ok = out->send();
	}

	return ok;
}

bool kdelafworker::getToolbarButtonImage()
{
	int width, height, tbHorW;
	bool enabled, pressed, rollover, focus, contentAreaFilled, borderPainted, horizontal;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readInt(tbHorW);
	ok = ok && in->readBool(enabled);
	ok = ok && in->readBool(pressed);
	ok = ok && in->readBool(rollover);
	ok = ok && in->readBool(focus);
	ok = ok && in->readBool(contentAreaFilled);
	ok = ok && in->readBool(borderPainted);
	ok = ok && in->readBool(horizontal);
	if (ok) {
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		KToolBarButton wrapper(this);
		wrapper.setText("");
		wrapper.setBackgroundOrigin(QWidget::ParentOrigin);
		wrapper.setGeometry(0, 0, width, height);
	
		int bStyle = QStyle::Style_Default | QStyle::Style_AutoRaise;
		int activeflags = QStyle::SC_None;
		if (enabled)
			bStyle = QStyle::Style_Enabled | QStyle::Style_Raised;
		if (pressed) {
			bStyle |= QStyle::Style_Down;
			activeflags = QStyle::SC_ToolButton;
		}
		if (rollover) {
			bStyle |= QStyle::Style_MouseOver;
			activeflags = QStyle::SC_ToolButton;
		}
		if (focus)
			bStyle |= bStyle | QStyle::Style_HasFocus;
		if (horizontal)
			bStyle |= QStyle::Style_Horizontal;
		QPixmap *bgPixmap = NULL;
		if (horizontal)
			bgPixmap = getToolbarBackground(width, tbHorW, horizontal, true);
		else
			bgPixmap = getToolbarBackground(tbHorW, height, horizontal, true);
		painter.drawPixmap(0, 0, *bgPixmap, 0, 0, width, height);
		if (contentAreaFilled) {
			style().drawComplexControl(QStyle::CC_ToolButton, &painter, &wrapper, QRect(0, 0, width, height), palette().active(), bStyle, activeflags);
		} else if (borderPainted) {
			painter.setPen(wrapper.paletteForegroundColor());
			painter.drawRect(0, 0, width, height);
		}
		painter.end();
		delete bgPixmap;
		
		out->writeImage(pixmap.convertToImage());
		ok = out->send();
	}
	return ok;
}


bool kdelafworker::getButtonPreferredSize()
{
	QString text;
	int iconWidth, iconHeight;
	bool ok = in->readString(text);
	ok = ok && in->readInt(iconWidth);
	ok = ok && in->readInt(iconHeight);
	if (ok) {
		KPushButton wrapper(this);
		if ( text.isEmpty() && ( (iconWidth <= 0) || (iconHeight <= 0) ) ) {
			iconWidth = 5;
			iconHeight = 5;
		}
		wrapper.setText(text);
		if ( (iconWidth > 0) && (iconHeight > 0) ) {
			QPixmap pixmap(iconWidth, iconHeight);
			if (text.isEmpty())
				wrapper.setPixmap(pixmap);
			else
				wrapper.setIconSet(QIconSet(pixmap));
		}
		QSize sz = wrapper.sizeHint();
		out->writeInt(sz.width());
		out->writeInt(sz.height());
		ok = out->send();
	}
	return ok;
}

bool kdelafworker::getToolbarButtonPreferredSize()
{
	QString text;
	int iconWidth, iconHeight;
	bool ok = in->readString(text);
	ok = ok && in->readInt(iconWidth);
	ok = ok && in->readInt(iconHeight);
	if (ok) {
		// KToolBarButton hides sizehint
		QToolButton wrapper(this);
		wrapper.setText(text);
		if ( (iconWidth > 0) && (iconHeight > 0) ) {
			QPixmap pixmap(iconWidth, iconHeight);
			if (text.isEmpty())
				wrapper.setPixmap(pixmap);
			else
				wrapper.setIconSet(QIconSet(pixmap));
		}
		QSize sz = wrapper.sizeHint();
		out->writeInt(sz.width());
		out->writeInt(sz.height());
		ok = out->send();
	}
	return ok;
}

bool kdelafworker::getTabImage()
{
	int width, height, tabIndex;
	bool enabled, selected;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readInt(tabIndex);
	ok = ok && in->readBool(enabled);
	ok = ok && in->readBool(selected);
	
	if (ok) {
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		QTabBar wrapper(this);
		QTab *aTab0 = new QTab("0"); aTab0->setIdentifier(0);
		QTab *aTab1 = new QTab("1"); aTab1->setIdentifier(1);
		QTab *aTab2 = new QTab("2"); aTab2->setIdentifier(2);
		wrapper.insertTab(aTab0, 0);
		wrapper.insertTab(aTab1, 0);
		wrapper.insertTab(aTab2, 0);

		int bStyle = QStyle::Style_Default;
		if (enabled) {
			bStyle = QStyle::Style_Enabled;
			if (selected)
				bStyle |= QStyle::Style_Selected;
		}
		
		const QPixmap *bgPixmap = paletteBackgroundPixmap();
		if ((bgPixmap != NULL) && (!bgPixmap->isNull())) {
			painter.fillRect(0, 0, width, height, QBrush(QColor(255,255,255), *bgPixmap));
		}
		else {
			painter.fillRect(0, 0, width, height, QBrush(wrapper.paletteBackgroundColor()));
		}
		style().drawControl(QStyle::CE_TabBarTab, &painter, &wrapper, QRect(0, 0, width, height), palette().active(), bStyle, QStyleOption(wrapper.tabAt(tabIndex)));
		painter.end();
		out->writeImage(pixmap.convertToImage());
		ok = out->send();
		/*
		[!] seems like delete wrapper; delete the tabs...
		delete aTab0;
		delete aTab1;
		delete aTab2;*/
	}

	return ok;
}

bool kdelafworker::getMenuItemImage(bool isCheckBox, bool isRadioButton)
{
	int width, height;
	bool enabled, rollover, checked;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(enabled);
	ok = ok && in->readBool(rollover);
	if (isCheckBox || isRadioButton)
		ok = ok && in->readBool(checked);
	else
		checked = false;

	if (ok) {
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		QPopupMenu *pm = new QPopupMenu(this);
		QMenuItem *mi = pm->findItem(pm->insertItem("", 0));

		if (isCheckBox || isRadioButton) {
			// [!] todo : When Qt provide exclusive check : use it
			pm->setCheckable(true);
			if (checked)
				pm->setItemChecked(0, true);
		}
		int bStyle = QStyle::Style_Default;
		if (enabled) {
			bStyle = QStyle::Style_Enabled;
			if (rollover) {
				bStyle |= QStyle::Style_Active;
			}
		}
		painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		style().drawControl(QStyle::CE_PopupMenuItem, &painter, pm, QRect(0, 0, width, height), palette().active(), bStyle, QStyleOption(mi, 16, 16));
		painter.end();
		out->writeImage(pixmap.convertToImage());
		ok = out->send();
		delete pm;
	}

	return ok;
}

bool kdelafworker::getCheckBoxMenuItemImage()
{
	return getMenuItemImage(true);
}

bool kdelafworker::getRadioButtonMenuItemImage()
{
	return getMenuItemImage(false, true);
}

bool kdelafworker::getMenuImage()
{
	int width, height;
	bool topLevel, enabled, selected;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(topLevel);
	ok = ok && in->readBool(enabled);
	ok = ok && in->readBool(selected);

	if (ok) {
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		
		int bStyle = QStyle::Style_Default;
		if (topLevel) {
			QMenuBar *mb = new QMenuBar(this);
			QMenuItem *mi = mb->findItem(mb->insertItem(""));
			if (enabled) {
				bStyle |= QStyle::Style_Enabled;
				if (selected) {
					bStyle |= QStyle::Style_Down | QStyle::Style_Active | QStyle::Style_HasFocus;
				}
			}
			style().drawControl(QStyle::CE_MenuBarItem, &painter, mb, QRect(0, 0, width, height), palette().active(), bStyle, QStyleOption(mi));
			painter.end();
			delete mb;
		}
		else {
			QPopupMenu *pm = new QPopupMenu(this);
			QMenuItem *mi = pm->findItem(pm->insertItem("", 0));
			if (enabled) {
				bStyle |= QStyle::Style_Enabled;
				if (selected) {
					bStyle |= QStyle::Style_Active;
				}
			}
			style().drawControl(QStyle::CE_PopupMenuItem, &painter, pm, QRect(0, 0, width, height), palette().active(), bStyle, QStyleOption(mi, 16, 16));
			painter.end();
			delete pm;
		}

		out->writeImage(pixmap.convertToImage());
		ok = out->send();
	}

	return ok;
}

bool kdelafworker::getMenuBarImage()
{
	int width, height;
	bool horizontal;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(horizontal);

	if (ok) {
		int sflags = QStyle::Style_Default;
		if (horizontal)
			sflags |= QStyle::Style_Horizontal;

		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		style().drawPrimitive(QStyle::PE_PanelMenuBar, &painter, QRect(0, 0, width, height), palette().active(), sflags);
		painter.end();
		out->writeImage(pixmap.convertToImage());
		ok = out->send();
	}

	return ok;
}


QPixmap *kdelafworker::getToolbarBackground(int width, int height, bool horizontal, bool full)
{
	int sflags = QStyle::Style_Enabled;		
	if (horizontal)
		sflags |= QStyle::Style_Horizontal;
	if (!full) {
		if (horizontal)
			width = 6;
		else
			height = 6;
	}
	QPixmap pixmap(width, height);
	QPainter painter(&pixmap);
		
	/*[!]QPixmap bgPixmap = FakeApp.getBackgroundPixamp();
	if ((bgPixmap != null) && (!bgPixmap.isNull()))
		painter.fillRect(0, 0,width, height, new QBrush(new QColor(255,255,255), bgPixmap));
	else*/
		painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
	style().drawPrimitive(QStyle::PE_PanelDockWindow, &painter, QRect(0, 0, width, height), palette().active(), sflags);
	style().drawPrimitive(QStyle::PE_DockWindowSeparator, &painter, QRect(0, 0, width, height), palette().active(), sflags);
	painter.end();

	QImage img = pixmap.convertToImage();
	for (int i = 1; i < width; i++)
		for (int j = 1; j < height; j++) {
			if (horizontal)
				img.setPixel(i, j, img.pixel(0, j));
			else
				img.setPixel(i, j, img.pixel(i, 0));
		}
	return new QPixmap(img);
}

bool kdelafworker::getToolBarSeparatorImage()
{
	int width, height;
	bool horizontal;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(horizontal);
	if (ok) {
		int sflags = QStyle::Style_Enabled;
		if (horizontal)
			sflags |= QStyle::Style_Horizontal;
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		/*[!]QPixmap bgPixmap = FakeApp.getBackgroundPixamp();
		if ((bgPixmap != null) && (!bgPixmap.isNull()))
			painter.fillRect(0, 0,width, height, new QBrush(new QColor(255,255,255), bgPixmap));
		else*/
			painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		
		style().drawPrimitive(QStyle::PE_DockWindowSeparator, &painter, QRect(0, 0, width, height), palette().active(), sflags);
		painter.end();
		out->writeImage(pixmap.convertToImage());
		ok = out->send();
	}

	return ok;
}

bool kdelafworker::getToolbarBackgroundImage()
{
	int width, height;
	bool horizontal;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(horizontal);
	if (ok) {
		QPixmap *bgPixmap = getToolbarBackground(width, height, horizontal);
		out->writeImage(bgPixmap->convertToImage());
		ok = out->send();
		delete bgPixmap;
	}

	return ok;
}

bool kdelafworker::getToolbarHandleImage()
{
	QToolBar widget(this);
	widget.setLabel("coucou");
	int width, height;
	bool horizontal;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(horizontal);
	if (ok) {
		int sflags = QStyle::Style_Enabled;
		if (horizontal)
			sflags |= QStyle::Style_Horizontal;
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		/*[!]QPixmap bgPixmap = FakeApp.getBackgroundPixamp();
		if ((bgPixmap != null) && (!bgPixmap.isNull()))
			painter.fillRect(0, 0,width, height, new QBrush(new QColor(255,255,255), bgPixmap));
		else*/
			painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		
		kStyle()->drawKStylePrimitive(KStyle::KPE_DockWindowHandle, &painter, &widget, QRect(0, 0, width, height), palette().active(), sflags);
		painter.end();
		out->writeImage(pixmap.convertToImage());
		ok = out->send();
	}

	return ok;
}

bool kdelafworker::getComboBoxImage()
{
	int width, height;
	bool editable, enabled, hasFocus, mouseOver;
	QString value;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(editable);
	ok = ok && in->readString(value);
	ok = ok && in->readBool(enabled);
	ok = ok && in->readBool(hasFocus);
	ok = ok && in->readBool(mouseOver);
	if (ok) {
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		QComboBox wrapper(editable, this);
		wrapper.resize(width, height);


		int sflags = enabled ? QStyle::Style_Enabled : QStyle::Style_Default;
		if (hasFocus)
			sflags |= QStyle::Style_HasFocus;
		int scflags = QStyle::SC_ComboBoxArrow | QStyle::SC_ComboBoxFrame | QStyle::SC_ComboBoxListBoxPopup | QStyle::SC_ComboBoxEditField;
		int activeflags = QStyle::SC_None;
		if (mouseOver)
			sflags |= QStyle::Style_MouseOver;

		painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		style().drawComplexControl(QStyle::CC_ComboBox, &painter, &wrapper, QRect(0, 0, width, height), palette().active(), sflags, scflags, activeflags);
		if (hasFocus)
			style().drawPrimitive(
				QStyle::PE_FocusRect,
				&painter,
				style().querySubControlMetrics(QStyle::CC_ComboBox, &wrapper, QStyle::SC_ComboBoxEditField),
				palette().active(),
				sflags);
		painter.end();
		out->writeImage(pixmap.convertToImage());
		ok = out->send();
	}
	return ok;
}

bool kdelafworker::getTableHeaderImage()
{
	int width, height;
	bool isPressed, isSelected, hasFocus;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(isPressed);
	ok = ok && in->readBool(isSelected);
	ok = ok && in->readBool(hasFocus);
	if (ok) {
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		int sflags = QStyle::Style_Enabled;
		if (isPressed)
			sflags |= QStyle::Style_Down;
		/*[!]QPixmap bgPixmap = FakeApp.getBackgroundPixamp();
		if ((bgPixmap != null) && (!bgPixmap.isNull()))
			painter.fillRect(0, 0,width, height, new QBrush(new QColor(255,255,255), bgPixmap));
		else*/
			painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		
		style().drawPrimitive(QStyle::PE_HeaderSection, &painter, QRect(0, 0, width, height), palette().active(), sflags);
		painter.end();
		out->writeImage(pixmap.convertToImage());
		ok = out->send();
	}
	return ok;
}

QImage *vScrollBar(const QImage &img, const QRect &r) {
	QImage *result = new QImage(img.height(), img.width(), img.depth(), img.numColors(), img.bitOrder());
	for (int i = 0; i < img.width(); i++)
		for (int j = 0; j < img.height(); j++) {
			if (r.contains(i, j))
				result->setPixel(j, img.width() - i - 1, img.pixel(i, j));
			else
				result->setPixel(img.height() - j - 1, img.width() - i - 1, img.pixel(i, j));
		}
	return result;
}

bool kdelafworker::getProgressBarImage()
{
	int width, height, value, length;
	bool isEnabled, hasFocus, horizonal;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(isEnabled);
	ok = ok && in->readBool(horizonal);
	ok = ok && in->readBool(hasFocus);
	ok = ok && in->readInt(value);
	ok = ok && in->readInt(length);

	if (ok) {
		QProgressBar wrapper(length, this);
		wrapper.setProgress(value);
		wrapper.setCenterIndicator(false);
		wrapper.setPercentageVisible(false);

		int sflags = QStyle::Style_Default;
		if (isEnabled) {
			sflags = QStyle::Style_Enabled;
			if (hasFocus)
			  sflags |= QStyle::Style_HasFocus;
		}

		if (!horizonal) {
			int tmp = width;
			width = height;
			height = tmp;
		}
		wrapper.resize(width, height);
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		if (horizonal) {
			style().drawControl(QStyle::CE_ProgressBarGroove, &painter, &wrapper, QRect(0, 0, width, height), palette().active(), sflags);
			style().drawControl(QStyle::CE_ProgressBarContents, &painter, &wrapper, QRect(0, 0, width, height), palette().active(), sflags);
			painter.end();
			out->writeImage(pixmap.convertToImage());
		}
		else {
			QPixmap pixmap1(width, height);
			QPainter painter1(&pixmap1);
			painter1.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
			style().drawControl(QStyle::CE_ProgressBarGroove, &painter1, &wrapper, QRect(0, 0, width, height), palette().active(), sflags);
			painter1.end();
			QWMatrix m;
			painter.drawPixmap(0, 0, pixmap1.xForm(m.rotate(180)));

			style().drawControl(QStyle::CE_ProgressBarContents, &painter, &wrapper, QRect(0, 0, width, height), palette().active(), sflags);
			painter.end();

			QRect r = style().subRect(QStyle::SR_ProgressBarContents, &wrapper);
			QImage *img = vScrollBar(pixmap.convertToImage(), r);
			out->writeImage(*img);
			delete img;
		}		
		ok = out->send();
		
	}
	return ok;
}

bool kdelafworker::getIndicatorImage(bool exclusive)
{
	int width, height;
	bool enabled, selected, rollover;
	bool ok = in->readBool(enabled);
	ok = ok && in->readBool(selected);
	ok = ok && in->readBool(rollover);
	if (ok) {
		QWidget *wrapper = NULL;
		if (exclusive) {
			width = style().pixelMetric(QStyle::PM_ExclusiveIndicatorWidth);
			height = style().pixelMetric(QStyle::PM_ExclusiveIndicatorHeight);
			wrapper = new QRadioButton(this);
		}
		else {
			width = style().pixelMetric(QStyle::PM_IndicatorHeight);
			height = style().pixelMetric(QStyle::PM_IndicatorWidth);
			wrapper = new QCheckBox(this);
		}
		int sflags = QStyle::Style_Default;
		if (enabled) {
			sflags |= QStyle::Style_Enabled;
			if (rollover)
				sflags |= QStyle::Style_MouseOver;
		}
		sflags |= selected ? QStyle::Style_On : QStyle::Style_Off;
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		/*[!]QPixmap bgPixmap = FakeApp.getBackgroundPixamp();
		if ((bgPixmap != null) && (!bgPixmap.isNull()))
			painter.fillRect(0, 0,width, height, new QBrush(new QColor(255,255,255), bgPixmap));
		else*/
			painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		if (exclusive)
			style().drawControl(QStyle::CE_RadioButton, &painter, wrapper, QRect(0, 0, width, height), palette().active(), sflags);
		else
			style().drawControl(QStyle::CE_CheckBox, &painter, wrapper, QRect(0, 0, width, height), palette().active(), sflags);
		painter.end();
		out->writeImage(pixmap.convertToImage());
		ok = out->send();
		delete wrapper;
	}
	return ok;
}

bool kdelafworker::getMenuButtonIndicatorImage()
{
	int width, height;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	if (ok) {
		int sflags = QStyle::Style_Enabled;
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		/*[!]QPixmap bgPixmap = FakeApp.getBackgroundPixamp();
		if ((bgPixmap != null) && (!bgPixmap.isNull()))
			painter.fillRect(0, 0,width, height, new QBrush(new QColor(255,255,255), bgPixmap));
		else*/
		painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		style().drawPrimitive(QStyle::PE_ArrowDown, &painter, QRect(0, 0, width, height), palette().active(), sflags);
		painter.end();
		out->writeImage(pixmap.convertToImage(), palette().active().background());
		ok = out->send();
	}

	return ok;
}

bool kdelafworker::getScrollBarThumbImage()
{
	int width, height;
	bool horizontal, highlighted;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(horizontal);
	ok = ok && in->readBool(highlighted);
	if (ok) {
		int sflags = QStyle::Style_Enabled;
		if (horizontal)
			sflags |= QStyle::Style_Horizontal;
		if (highlighted)
			sflags |= QStyle::Style_Down;
		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);
		
		style().drawPrimitive(QStyle::PE_ScrollBarSlider, &painter, QRect(0, 0, width, height), palette().active(), sflags);
		painter.end();
		out->writeImage(pixmap.convertToImage());
		ok = out->send();
	}

	return ok;
}

bool kdelafworker::getScrollBarMetrics()
{
	int width, height, min, max, val, pageStep;
	bool horizontal;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(horizontal);
	ok = ok && in->readInt(min);
	ok = ok && in->readInt(max);
	ok = ok && in->readInt(val);
	ok = ok && in->readInt(pageStep);
	if (ok) {
		QScrollBar wrapper(this);
		wrapper.setOrientation(horizontal ? Qt::Horizontal : Qt::Vertical);
		wrapper.resize(width, height);
		wrapper.setMinValue(min);
		wrapper.setMaxValue(max);
		wrapper.setValue(val);
		wrapper.setPageStep(pageStep);

		QRect  addline, subline, addpage, subpage, first, last, slider, groove;
		subline = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarSubLine);
		addline = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarAddLine);
		subpage = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarSubPage);
		addpage = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarAddPage);
		first   = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarFirst);
		last    = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarLast);
		slider   = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarSlider);
		groove    = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarGroove);

		out->writeRect(subline);
		out->writeRect(addline);
		out->writeRect(subpage);
		out->writeRect(addpage);
		out->writeRect(first);
		out->writeRect(last);
		out->writeRect(slider);
		out->writeRect(groove);
		ok = out->send();
	}

	return ok;
}

bool kdelafworker::getScrollBarImage()
{
	int width, height, min, max, val, pageStep;
	bool horizontal, enabled, subPressed, addPressed, sliderPressed;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(horizontal);
	ok = ok && in->readBool(enabled);
	ok = ok && in->readInt(min);
	ok = ok && in->readInt(max);
	ok = ok && in->readInt(val);
	ok = ok && in->readInt(pageStep);
	ok = ok && in->readBool(subPressed);
	ok = ok && in->readBool(addPressed);
	ok = ok && in->readBool(sliderPressed);
	if (ok) {
		QScrollBar wrapper(this);
		wrapper.setOrientation(horizontal ? Qt::Horizontal : Qt::Vertical);
		wrapper.resize(width, height);
		wrapper.setMinValue(min);
		wrapper.setMaxValue(max);
		wrapper.setValue(val);
		wrapper.setPageStep(pageStep);
		wrapper.setEnabled(enabled);
		int sflags = QStyle::Style_Default;
		if (enabled)
			sflags |= QStyle::Style_Enabled;
		if (horizontal)
			sflags |= QStyle::Style_Horizontal;
		int subActive = QStyle::SC_None;
		if (subPressed)
			subActive = QStyle::SC_ScrollBarSubLine;
		else if (addPressed)
			subActive = QStyle::SC_ScrollBarAddLine;
		else if (sliderPressed)
			subActive = QStyle::SC_ScrollBarSlider;

		QPixmap pixmap(width, height);
		QPainter painter(&pixmap);

		painter.fillRect(0, 0, width, height, palette().active().brush(QColorGroup::Background));
		style().drawComplexControl(QStyle::CC_ScrollBar, &painter, &wrapper, QRect(0, 0, width, height), palette().active(), sflags, QStyle::SC_All, subActive);
		painter.end();

		out->writeImage(pixmap.convertToImage());
		ok = out->send();
	}

	return ok;
}

bool kdelafworker::getScrollBarTrackImage()
{
	int width, height, wWidth, wHeight;
	bool horizontal;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(horizontal);
	if (ok) {
		QScrollBar wrapper(this);
		wrapper.setOrientation(horizontal ? Qt::Horizontal : Qt::Vertical);
		
		QRect addline, subline, slider;
		subline = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarSubLine);
		addline = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarAddLine);
		
		int sflags = QStyle::Style_Enabled;
		if (horizontal) {
			sflags |= QStyle::Style_Horizontal;
			wHeight = height;
			wWidth = width + subline.width() + addline.width();
		}
		else {
			wWidth = width;
			wHeight = height + subline.height() + addline.height();
		}
		wrapper.resize(wWidth, wHeight);
		slider  = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarSlider);

		int draw = QStyle::SC_ScrollBarSubLine |
			QStyle::SC_ScrollBarAddLine |
			QStyle::SC_ScrollBarSubPage |
			QStyle::SC_ScrollBarAddPage |
			//QStyle::SC_ScrollBarSlider |
			QStyle::SC_ScrollBarFirst |
			QStyle::SC_ScrollBarLast;
		QPixmap pixmap(wWidth, wHeight);
		QPainter painter(&pixmap);
		painter.fillRect(0, 0, wWidth, wHeight, palette().active().brush(QColorGroup::Background));
		style().drawComplexControl(QStyle::CC_ScrollBar, &painter, &wrapper, QRect(0, 0, wWidth, wHeight), palette().active(), sflags, draw);
		painter.end();

		wrapper.setValue(wrapper.maxValue());
		QPixmap pixmap2(wWidth, wHeight);
		QPainter painter2(&pixmap2);
		painter2.fillRect(0, 0, wWidth, wHeight, palette().active().brush(QColorGroup::Background));
		style().drawComplexControl(QStyle::CC_ScrollBar, &painter2, &wrapper, QRect(0, 0, wWidth, wHeight), palette().active(), sflags, draw);
		painter2.end();
		
		bitBlt(&pixmap, slider.x(), slider.y(), &pixmap2, slider.x(), slider.y(), slider.width(), slider.height(), Qt::CopyROP);
		QPixmap truePixmap(width, height);
		if (horizontal) {
			bitBlt(&truePixmap, 0, 0, &pixmap, subline.x() + subline.width(), subline.y(), wWidth - addline.width() - subline.width(), subline.height(), Qt::CopyROP);
		}
		else {
			bitBlt(&truePixmap, 0, 0, &pixmap, subline.x(), subline.y() + subline.height(), subline.width(), wHeight - subline.height() - addline.height(), Qt::CopyROP);
		}
		out->writeImage(truePixmap.convertToImage());
		ok = out->send();
	}

	return ok;
}

bool kdelafworker::getScrollBarNextImage()
{
	int index;
	bool horizontal;
	bool ok = in->readBool(horizontal);
	ok = ok && in->readInt(index);

	if (ok) {
		QScrollBar wrapper(this);
		wrapper.setOrientation(horizontal ? Qt::Horizontal : Qt::Vertical);
		/*wrapper.setMinValue(0);
		wrapper.setMaxValue(65535);
		wrapper.setValue(32767);
		wrapper.setPageStep(1);*/
		wrapper.resize(wrapper.sizeHint().width(), wrapper.sizeHint().height());
		QRect addline = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarAddLine);

		int sflags = QStyle::Style_Enabled;
		if (horizontal)
			sflags |= QStyle::Style_Horizontal;
		int subActive = QStyle::SC_None;
		if (index == 1)
			subActive = QStyle::SC_ScrollBarAddLine;
		else if (index == 2)
			subActive = QStyle::SC_ScrollBarSubLine;
		QPixmap pixmap(wrapper.width(), wrapper.height());
		QPainter painter(&pixmap);
		style().drawComplexControl(QStyle::CC_ScrollBar, &painter, &wrapper, QRect(0, 0, wrapper.width(), wrapper.height()), palette().active(), sflags, QStyle::SC_All, subActive);
		painter.end();
		
		QPixmap truePixmap(addline.width(), addline.height());
		bitBlt(&truePixmap, 0, 0, &pixmap, addline.x(), addline.y(), addline.width(), addline.height(), Qt::CopyROP);
		out->writeImage(truePixmap.convertToImage());
		ok = out->send();
	}
	return ok;
}

bool kdelafworker::getScrollBarPrevImage()
{
	int index;
	bool horizontal, treeButtons;
	bool ok = in->readBool(horizontal);
	ok = ok && in->readInt(index);
	ok = in->readBool(treeButtons);

	if (ok) {
		QScrollBar wrapper(this);
		wrapper.setOrientation(horizontal ? Qt::Horizontal : Qt::Vertical);
		//wrapper.polish();
		
			//std::cerr << wrapper.sizeHint().width() << ", " << wrapper.sizeHint().height() << "\n";
		wrapper.resize(wrapper.sizeHint().width(), wrapper.sizeHint().height());
		QRect subline = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarSubLine);
		QRect addline = style().querySubControlMetrics(QStyle::CC_ScrollBar, &wrapper, QStyle::SC_ScrollBarAddLine);
		//QRect subline2;
		/*if (treeButtons) {
			subline2 = addline;
			if (horizontal)
				subline2.moveBy(-addline.width(), 0);
			else
				subline2.moveBy(0, -addline.height());
			if (subline2.isValid()) {
				style().drawPrimitive(QStyle::PE_ScrollBarSubLine, &painter, subline2, palette().active(), sflags);
			}
		}*/

		int sflags = QStyle::Style_Enabled;
		if (horizontal)
			sflags |= QStyle::Style_Horizontal;
		int subActive = QStyle::SC_None;
		if (index == 1)
			subActive = QStyle::SC_ScrollBarAddLine;
		else if (index == 2)
			subActive = QStyle::SC_ScrollBarSubLine;
		QPixmap pixmap(wrapper.width(), wrapper.height());
		QPainter painter(&pixmap);
		style().drawComplexControl(QStyle::CC_ScrollBar, &painter, &wrapper, QRect(0, 0, wrapper.width(), wrapper.height()), palette().active(), sflags, QStyle::SC_All, subActive);
		/*
		if (treeButtons) {
			QRect subline2 = addline;
			if (horizontal)
				subline2.moveBy(-addline.width(), 0);
			else
				subline2.moveBy(0, -addline.height());
			if (subline2.isValid()) {
				style().drawPrimitive(QStyle::PE_ScrollBarSubLine, &painter, subline2, palette().active(), sflags);
			}
		}
*/
		painter.end();
		
			
		QPixmap truePixmap(subline.width(), subline.height());
		bitBlt(&truePixmap, 0, 0, &pixmap, subline.x(), subline.y(), subline.width(), subline.height(), Qt::CopyROP);
		//if (horizontal)
			out->writeImage(truePixmap.convertToImage());
		/*else {
			QWMatrix mat;
			mat = mat.rotate(90.0);
			std::cerr << subline.width() << ", " << subline.height() << " => ";
			QImage tmp = truePixmap.convertToImage().xForm(mat);
			std::cerr << tmp.width() << ", " << tmp.height() << "\n";
			out->writeImage(tmp);
		}*/
		ok = out->send();
	}
	return ok;
}

bool kdelafworker::getSpinnerImage()
{
	int index;
	bool isNext;
	bool ok = in->readInt(index);
	ok = ok && in->readBool(isNext);
	if (ok) {
		QStyle::SFlags sflags = QStyle::Style_Default;
		QStyle::SCFlags subActive = QStyle::SC_None;
		if (index < 2) {
			sflags |= QStyle::Style_Enabled;
			if (index == 1)
				subActive = isNext ? QStyle::SC_SpinWidgetUp : QStyle::SC_SpinWidgetDown;
		}
		QSpinBox wrapper(this);
		wrapper.resize(wrapper.sizeHint());
		QRect subSize;
		if (isNext)
			subSize = style().querySubControlMetrics(QStyle::CC_SpinWidget, &wrapper, QStyle::SC_SpinWidgetUp);
		else
			subSize = style().querySubControlMetrics(QStyle::CC_SpinWidget, &wrapper, QStyle::SC_SpinWidgetDown);
	
		QPixmap pixmap(subSize.width(), subSize.height());
		QPainter painter(&pixmap);
		painter.fillRect(0, 0, subSize.width(), subSize.height(), palette().active().brush(QColorGroup::Background));
		style().drawComplexControlMask(QStyle::CC_SpinWidget, &painter, &wrapper, subSize);
		style().drawPrimitive((isNext ? QStyle::PE_SpinWidgetUp : QStyle::PE_SpinWidgetDown), &painter, QRect(0,0,subSize.width(), subSize.height()), palette().active(), sflags);
		painter.end();
		out->writeImage(pixmap.convertToImage());
		ok = out->send();
	}

	return ok;

/*	int index;
	bool isNext;
	bool ok = in->readInt(index);
	ok = ok && in->readBool(isNext);
	if (ok) {
		int sflags = QStyle::Style_Default;
		int subActive = QStyle::SC_None;
		if (index < 2) {
			sflags |= QStyle::Style_Enabled;
			if (index == 1)
				subActive = isNext ? QStyle::SC_SpinWidgetUp : QStyle::SC_SpinWidgetDown;
		}
		
		QSpinBox wrapper(this);
		wrapper.resize(wrapper.sizeHint());
		QRect subSize;
		if (isNext)
			subSize = style().querySubControlMetrics(QStyle::CC_SpinWidget, &wrapper, QStyle::SC_SpinWidgetUp);
		else
			subSize = style().querySubControlMetrics(QStyle::CC_SpinWidget, &wrapper, QStyle::SC_SpinWidgetDown);

		QPixmap pixmap(wrapper.width(), wrapper.height());
		QPainter painter(&pixmap);
		
		style().drawComplexControl(QStyle::CC_SpinWidget, &painter, &wrapper, QRect(0, 0, wrapper.width(), wrapper.height()), palette().active(), sflags, QStyle::SC_All, subActive);
		painter.end();
		
		QPixmap truePixmap(subSize.width(), subSize.height());
		bitBlt(&truePixmap, 0, 0, &pixmap, subSize.x(), subSize.y(), subSize.width(), subSize.height(), Qt::CopyROP);
		
		out->writeImage(truePixmap.convertToImage());
		ok = out->send();
	}

	return ok;*/
}

bool kdelafworker::getSliderGrooveImage()
{
	bool enabled;
	bool horizontal;
	bool focus;
	bool ok = in->readBool(enabled);
	ok = ok && in->readBool(horizontal);
	ok = ok && in->readBool(focus);
	if (ok) {
		QSlider wrapper(0, 100, 10, 50, horizontal ? Qt::Horizontal : Qt::Vertical, this);
		wrapper.setBackgroundOrigin(QWidget::ParentOrigin);
		wrapper.setEnabled(enabled);
		wrapper.resize(wrapper.sizeHint());
		QRect subSize = style().querySubControlMetrics(QStyle::CC_Slider, &wrapper, QStyle::SC_SliderGroove);
		//std::cerr << focus << "=>";
		if (focus) {
			//[!] doesn't work => hack on java part
			wrapper.setFocus();
			while (app->hasPendingEvents())
				app->processEvents();
		}
		//std::cerr << wrapper.hasFocus() << "\n";
		wrapper.setValue(0);
		QPixmap pixmap1 = QPixmap::grabWidget(&wrapper);
		wrapper.setValue(100);
		QPixmap pixmap2 = QPixmap::grabWidget(&wrapper);

		if (horizontal) {
			int breakPoint = subSize.width() / 2;
			QPixmap truePixmap(subSize.width(), subSize.height());
			bitBlt(&truePixmap, breakPoint, 0, &pixmap1, subSize.x() + breakPoint, subSize.y(), subSize.width() - breakPoint, subSize.height(), Qt::CopyROP);
			bitBlt(&truePixmap, 0, 0, &pixmap2, subSize.x(), subSize.y(), breakPoint, subSize.height(), Qt::CopyROP);
			out->writeImage(truePixmap.convertToImage());
		}
		else {
			int breakPoint = subSize.height() / 2;
			QPixmap truePixmap(subSize.width(), subSize.height());
			bitBlt(&truePixmap, 0, breakPoint, &pixmap1, subSize.x(), subSize.y() + breakPoint, subSize.width(), subSize.height() - breakPoint, Qt::CopyROP);
			bitBlt(&truePixmap, 0, 0, &pixmap2, subSize.x(), subSize.y(), subSize.width(), breakPoint, Qt::CopyROP);
			out->writeImage(truePixmap.convertToImage());
		}
		ok = out->send();
	}
	return ok;
}

bool kdelafworker::getSliderHandleImage()
{
	bool enabled;
	bool horizontal;
	bool selected;
	bool focus;
	bool ok = in->readBool(enabled);
	ok = ok && in->readBool(horizontal);
	ok = ok && in->readBool(selected);
	ok = ok && in->readBool(focus);
	if (ok) {
		QSlider wrapper(0, 100, 10, 50, horizontal ? Qt::Horizontal : Qt::Vertical, this);
	//	wrapper.setBackgroundOrigin(QWidget::ParentOrigin);
		wrapper.setPaletteForegroundColor(QColor(0, 255, 0));
		wrapper.setEnabled(enabled);
		wrapper.resize(wrapper.sizeHint());
		QRect subSize = style().querySubControlMetrics(QStyle::CC_Slider, &wrapper, QStyle::SC_SliderHandle);
		if (selected) {
			QMouseEvent event(QEvent::MouseButtonPress, subSize.center(), Qt::NoButton, Qt::LeftButton);
			KApplication::sendEvent(&wrapper, &event);
		}
		wrapper.setValue(50);
		QPixmap pixmap = QPixmap::grabWidget(&wrapper);
		QPixmap truePixmap(subSize.width(), subSize.height());
		bitBlt(&truePixmap, 0, 0, &pixmap, subSize.x(), subSize.y(), subSize.width(), subSize.height(), Qt::CopyROP);
		out->writeImage(truePixmap.convertToImage(), wrapper.paletteBackgroundColor());
		ok = out->send();
	}
	return ok;
}

bool kdelafworker::getThumbTrackOffset()
{
	bool horizontal;
	bool ok = in->readBool(horizontal);
	if (ok) {
		QSlider wrapper(0, 100, 10, 50, horizontal ? Qt::Horizontal : Qt::Vertical, this);
		wrapper.setBackgroundOrigin(QWidget::ParentOrigin);
		wrapper.setEnabled(true);
		wrapper.resize(wrapper.sizeHint());
		wrapper.setValue(50);
		QRect thumb = style().querySubControlMetrics(QStyle::CC_Slider, &wrapper, QStyle::SC_SliderHandle);
		QRect track = style().querySubControlMetrics(QStyle::CC_Slider, &wrapper, QStyle::SC_SliderGroove);
		int val = 0;
		if (horizontal)
			val = track.y() - thumb.y();
		else
			val = track.x() - thumb.x();

		out->writeInt(val);
		ok = out->send();
	}
	return ok;
}

bool kdelafworker::getTitleButtonImage()
{
	int JSC;
	bool clicked, rollover;
	bool ok = in->readInt(JSC);
	ok = ok && in->readBool(clicked);
	ok = ok && in->readBool(rollover);
	if (ok) {
		QMainWindow tmp(this, "foo");
		QTitleBar wrapper(&tmp, this, "foo");
		int sflags = QStyle::Style_Enabled;
		if (rollover)
			sflags |= QStyle::Style_MouseOver;
		QStyle::SubControl subControl = JSCtoQSC(JSC);
		QStyle::SubControl subActive = (clicked) ? subControl : QStyle::SC_None;
		QRect subSize;
		subSize = style().querySubControlMetrics(QStyle::CC_TitleBar, &wrapper, subControl);
		
		QPixmap pixmap(wrapper.width(), wrapper.height());
		QPainter painter(&pixmap);
		
		painter.fillRect(0, 0, wrapper.width(), wrapper.height(), palette().active().brush(QColorGroup::Background));
		style().drawComplexControl(QStyle::CC_TitleBar, &painter, &wrapper, QRect(0, 0, wrapper.width(), wrapper.height()), palette().active(), sflags, subControl, subActive);
		painter.end();
		
		QPixmap truePixmap(subSize.width(), subSize.height());
		bitBlt(&truePixmap, 0, 0, &pixmap, subSize.x(), subSize.y(), subSize.width(), subSize.height(), Qt::CopyROP);
		
		out->writeImage(truePixmap.convertToImage(), palette().active().background());
		ok = out->send();
	}

	return ok;
}

bool kdelafworker::getCommonImage()
{
	int key;
	bool ok = in->readInt(key);
	if (ok) {
		QImage *img = NULL;
		switch (key) {	
			case CI_ERROR_ICON:
				img = new QImage(style().stylePixmap(QStyle::SP_MessageBoxCritical).convertToImage());
				break;
			case CI_INFO_ICON:
				img = new QImage(style().stylePixmap(QStyle::SP_MessageBoxInformation).convertToImage());
				break;
			case CI_WARNING_ICON:
				img = new QImage(style().stylePixmap(QStyle::SP_MessageBoxWarning).convertToImage());
				break;
			case CI_QUESTION_ICON:
				img = new QImage(style().stylePixmap(QStyle::SP_MessageBoxQuestion).convertToImage());
				break;
			case CI_MAXIMIZE_ICON:
				img = new QImage(style().stylePixmap(QStyle::SP_TitleBarMaxButton).convertToImage());
				break;
			case CI_MINIMIZE_ICON:
				img = new QImage(style().stylePixmap(QStyle::SP_TitleBarMinButton).convertToImage());
				break;
			case CI_CLOSE_ICON:
				img = new QImage(style().stylePixmap(QStyle::SP_TitleBarCloseButton).convertToImage());
				break;
			case CI_ICONIFY_ICON:
				img = new QImage(style().stylePixmap(QStyle::SP_TitleBarShadeButton).convertToImage());
				break;
			case CI_TREEEXPANDER_ON:
			case CI_TREEEXPANDER_OFF:
			case CI_TREEBRANCH_H:
			case CI_TREEBRANCH_V:
				if (key == CI_TREEEXPANDER_ON) {
					int width = 9;
					int height = 9;
					KListView wrapper(this, "foo");
					QBrush br(wrapper.palette().active().base());
					QPixmap pixmap(width, height);
					QPainter painter(&pixmap);
					painter.fillRect(0, 0, width, height, br);
					kStyle()->drawKStylePrimitive(KStyle::KPE_ListViewExpander, &painter, NULL, QRect(0, 0, width, height), wrapper.palette().active(), KStyle::Style_On);
					painter.end();
					img = new QImage(pixmap.convertToImage());
				}
				else if (key == CI_TREEEXPANDER_OFF) {
					int width = 9;
					int height = 9;
					KListView wrapper(this, "foo");
					QBrush br(wrapper.palette().active().base());
					QPixmap pixmap(width, height);
					QPainter painter(&pixmap);
					painter.fillRect(0, 0, width, height, br);
					kStyle()->drawKStylePrimitive(KStyle::KPE_ListViewExpander, &painter, NULL, QRect(0, 0, width, height), wrapper.palette().active(), KStyle::Style_Off);
					painter.end();
					img = new QImage(pixmap.convertToImage());
				}
				else if (key == CI_TREEBRANCH_H) {
					int width = 128;
					int height = kStyle()->kPixelMetric(KStyle::KPM_ListViewBranchThickness);
					KListView wrapper(this, "foo");
					QBrush br(wrapper.palette().active().base());
					QPixmap pixmap(width, height);
					QPainter painter(&pixmap);
					painter.fillRect(0, 0, width, height, br);
					kStyle()->drawKStylePrimitive(KStyle::KPE_ListViewBranch, &painter, NULL, QRect(0, 0, width, height), wrapper.palette().active(), KStyle::Style_Horizontal);
					painter.end();
					img = new QImage(pixmap.convertToImage());
				}
				else if (key == CI_TREEBRANCH_V) {
					int width = kStyle()->kPixelMetric(KStyle::KPM_ListViewBranchThickness);
					int height = 129;
					KListView wrapper(this, "foo");
					QBrush br(wrapper.palette().active().base());
					QPixmap pixmap(width, height);
					QPainter painter(&pixmap);
					painter.fillRect(0, 0, width, height, br);
					kStyle()->drawKStylePrimitive(KStyle::KPE_ListViewBranch, &painter, NULL, QRect(0, 0, width, height), wrapper.palette().active());
					painter.end();
					img = new QImage(pixmap.convertToImage());
				}
				break;
			default : // just in case
				img = new QImage(style().stylePixmap(QStyle::SP_MessageBoxCritical).convertToImage());
		}
		out->writeImage(*img);
		ok = out->send();
		delete img;
	}
	return ok;
}

/*!
    \fn kdelafworker::getPixelMetric()
 */
bool kdelafworker::getPixelMetric()
{
	int JPM;
	bool ok = in->readInt(JPM);
	if (ok) {
		int QPM = JPMtoQPM(JPM);
		int pm = 0;
		if (QPM == JPM_TableHeaderH) {
			QHeader wrapper(this);
			pm = wrapper.sizeHint().height();
		}
		else if (QPM == JPM_ButtonH) {
			KPushButton wrapper(this);
			pm = wrapper.sizeHint().height();
		}
		else {
			pm = style().pixelMetric((QStyle::PixelMetric)QPM);
		}
		out->writeInt(pm);
		ok = out->send();
	}
	return ok;
}

bool kdelafworker::getToolTipDefaults()
{
	QToolTipHack hack(this);	
	QColor background = hack.palette().active().background();
	QColor foreground = hack.palette().active().foreground();
	QFont font = hack.font();
	QColor backgroundInactive = hack.palette().inactive().background();
	QColor foregroundInactive = hack.palette().inactive().foreground();
	out->writeColor(background);
	out->writeColor(foreground);
	out->writeFont(font);
	out->writeColor(backgroundInactive);
	out->writeColor(foregroundInactive);
	return out->send();
}

bool kdelafworker::getLocalePaths()
{
	QStringList localePaths = KGlobal::dirs()->resourceDirs("locale");
	out->writeStrings(localePaths);
	return out->send();
}

bool kdelafworker::getMimePaths()
{
	QStringList mimePaths = KGlobal::dirs()->resourceDirs("mime");
	out->writeStrings(mimePaths);
	return out->send();
}

bool kdelafworker::getConfigPaths()
{
	QStringList configPaths = KGlobal::dirs()->resourceDirs("config");
	out->writeStrings(configPaths);
	return out->send();
}

bool kdelafworker::getTextFieldDefaults()
{
	QLineEdit wrapper(this);
	QColor background = wrapper.palette().active().base();
	QColor foreground = wrapper.palette().active().text();
	QFont font = wrapper.font();
	QColor backgroundInactive = wrapper.palette().inactive().base();
	QColor foregroundInactive = wrapper.palette().inactive().text();
	QColor selectionBackground = wrapper.palette().active().highlight();
	QColor selectionForeground = wrapper.palette().active().highlightedText();
	out->writeColor(background);
	out->writeColor(foreground);
	out->writeFont(font);
	out->writeColor(backgroundInactive);
	out->writeColor(foregroundInactive);
	out->writeColor(selectionBackground);
	out->writeColor(selectionForeground);
	return out->send();
}

bool kdelafworker::getLabelDefaults()
{
	QLabel wrapper(this);
	QColor background = wrapper.palette().active().background();
	QColor foreground = wrapper.palette().active().foreground();
	QFont font = wrapper.font();
	QColor disabledForeground = wrapper.palette().inactive().foreground();
	QColor disabledShadow = wrapper.palette().inactive().shadow();
	out->writeColor(background);
	out->writeColor(foreground);
	out->writeFont(font);
	out->writeColor(disabledForeground);
	out->writeColor(disabledShadow);
	return out->send();
}

bool kdelafworker::getComboBoxDefaults()
{
	QLabel wrapper(this);
	QColor background = wrapper.palette().active().base();
	QColor foreground = wrapper.palette().active().text();
	QFont font = wrapper.font();
	QColor selectionBackground = wrapper.palette().inactive().highlight();
	QColor selectionForeground = wrapper.palette().inactive().highlightedText();
	QColor disabledBackground = wrapper.palette().inactive().base();
	QColor disabledForeground = wrapper.palette().inactive().text();
	out->writeColor(background);
	out->writeColor(foreground);
	out->writeFont(font);
	out->writeColor(selectionBackground);
	out->writeColor(selectionForeground);
	out->writeColor(disabledBackground);
	out->writeColor(disabledForeground);
	return out->send();
}

bool kdelafworker::getTreeDefaults()
{ 
	KListView wrapper(this, "foo");	
	QColor background = wrapper.palette().active().base(); // [!] Not sure for this value
	QColor foreground = wrapper.palette().active().foreground();
	QFont font = wrapper.font();
	QColor textBackground = wrapper.palette().active().base(); // [!] Not sure for this value
	QColor textForeground = wrapper.palette().active().text();
	QColor selectionBackground = wrapper.palette().active().highlight();
	QColor selectionForeground = wrapper.palette().active().highlightedText();
	out->writeColor(background);
	out->writeColor(foreground);
	out->writeFont(font);
	out->writeColor(textBackground);
	out->writeColor(textForeground);
	out->writeColor(selectionBackground);
	out->writeColor(selectionForeground);
	return out->send();
}

bool kdelafworker::getIconPaths()
{
	QStringList iconPaths = KGlobal::dirs()->resourceDirs("icon");
	for (QStringList::Iterator it = iconPaths.begin(); it != iconPaths.end(); ++it) {
		*it += KIconTheme::current();
    	}
	out->writeStrings(iconPaths);
	return out->send();
}

bool kdelafworker::getComboBoxSize()
{
	bool editable;
	QString value;
	bool ok = in->readBool(editable);
	ok = ok && in->readString(value);
	if (ok) {
		QComboBox wrapper(editable, this);
		wrapper.insertItem(value);
		wrapper.setCurrentText(value);
		out->writeInt(wrapper.sizeHint().width());
		out->writeInt(wrapper.sizeHint().height());
		ok = out->send();
	}
	return ok;
}

bool kdelafworker::getIndicatorSize(bool exclusive)
{
	int width, height;
	if (exclusive) {
		width = style().pixelMetric(QStyle::PM_ExclusiveIndicatorWidth);
		height = style().pixelMetric(QStyle::PM_ExclusiveIndicatorHeight);
	}
	else {
		width = style().pixelMetric(QStyle::PM_IndicatorHeight);
		height = style().pixelMetric(QStyle::PM_IndicatorWidth);
	}
	out->writeInt(width);
	out->writeInt(height);
	return out->send();
}

bool kdelafworker::getComboValueRect()
{
	int width, height;
	bool editable;
	QString value;
	bool ok = in->readInt(width);
	ok = ok && in->readInt(height);
	ok = ok && in->readBool(editable);
	ok = ok && in->readString(value);
	if (ok) {
		QComboBox wrapper(editable, this);
		wrapper.insertItem(value);
		wrapper.setCurrentText(value);
		wrapper.resize(width, height);
		QRect rect = style().querySubControlMetrics(QStyle::CC_ComboBox, &wrapper, QStyle::SC_ComboBoxEditField);
		out->writeRect(rect);
		ok = out->send();
	}
	return ok;
}

int kdelafworker::JPMtoQPM(int JPM)
{
	int QPM;
	switch (JPM) {
		case JPM_TabBarTabVSpace:
			QPM = QStyle::PM_TabBarTabVSpace;
			break;
		case JPM_TabBarTabHSpace:
			QPM = QStyle::PM_TabBarTabHSpace;
			break;
		case JPM_MenuButtonIndicator:
			QPM = QStyle::PM_MenuButtonIndicator;
			break;
		case JPM_DockWindowSeparatorExtent:
			QPM = QStyle::PM_DockWindowSeparatorExtent;
			break;
		case JPM_DockWindowHandleExtent:
			QPM = QStyle::PM_DockWindowHandleExtent;
			break;
		case JPM_TableHeaderH:
			QPM = JPM_TableHeaderH;
			break;
		case JPM_ButtonH:
			QPM = JPM_ButtonH;
			break;
		case JPM_ButtonShiftHorizontal:
			QPM = QStyle::PM_ButtonShiftHorizontal;
			break;
		case JPM_ButtonShiftVertical:
			QPM = QStyle::PM_ButtonShiftVertical;
			break;
		default:
			QPM = QStyle::PM_ButtonMargin;
	}
	return QPM;
}

QStyle::SubControl kdelafworker::JSCtoQSC(int JSC)
{
	QStyle::SubControl QSC;
	switch (JSC) {
		case JSC_TitleBarSysMenu:
			QSC = QStyle::SC_TitleBarSysMenu;
			break;
		case JSC_TitleBarMinButton:
			QSC = QStyle::SC_TitleBarMinButton;
			break;
		case JSC_TitleBarMaxButton:
			QSC = QStyle::SC_TitleBarMaxButton;
			break;
		case JSC_TitleBarCloseButton:
			QSC = QStyle::SC_TitleBarCloseButton;
			break;
		case JSC_TitleBarLabel:
			QSC = QStyle::SC_TitleBarLabel;
			break;
		case JSC_TitleBarNormalButton:
			QSC = QStyle::SC_TitleBarNormalButton;
			break;
		case JSC_TitleBarShadeButton:
			QSC = QStyle::SC_TitleBarShadeButton;
			break;
		default:
			QSC = QStyle::SC_TitleBarUnshadeButton;
	}
	return QSC;
}

#include "kdelafworker.moc"
