#ifndef QTOOLTIPHACK_H
#define QTOOLTIPHACK_H

#include <qtooltip.h>

/**
	@author Sekou DIAKITE <blunted@blinux>
*/
class QToolTipHack : public QToolTip
{

public:
	QToolTipHack(QWidget * widget) : QToolTip(widget)
	{
	}
protected:
	void maybeTip( const QPoint & )
	{
	}
};

#endif
