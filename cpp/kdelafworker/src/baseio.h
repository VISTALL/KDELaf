#ifndef BASEIO_H
#define BASEIO_H

#include <qfont.h>
#include <qcolor.h>
#include <qimage.h>
#include <qcstring.h>
#include <qstringlist.h>
#include "typedef.h"

/**
	@author Sekou DIAKITE <blunted@blinux>
*/
class InBuffer
{
public:
	InBuffer(bool isLittleEndian);
	~InBuffer();
	bool receive();
	bool readInt(int32 &val);
	bool readChar(QChar &val);
	bool readByte(int8 &val);
	bool readUnsignedByte(uint8 &val);
	bool readBool(bool &val);
	bool readString(QString &val);
	bool readColor(QColor &val);
	bool readFont(QFont &val);
	int totalSize();
	int unreadSize();
protected:
	void swap(char &_1, char &_2);
	void swap(char *buff, int length);
	bool read(char *data, int length);
protected:
	bool isLittleEndian;
};

class OutBuffer
{
public:
	OutBuffer(bool isLittleEndian);
	~OutBuffer();
	bool send();
	void writeInt(int32 val);
	void writeChar(const QChar &val);
	void writeByte(int8 val);
	void writeUnsignedByte(uint8 val);
	void writeBool(bool val);
	void writeString(const QString &val);
	void writeColor(const QColor &val);
	void writeFont(const QFont &val);
	void writeStrings(QStringList &val);
	void writeRect(const QRect &val);
	void writeImage(const QImage &val);
	void writeImage(const QImage &val, const QColor &transparentColor);
protected:
	void swap(char &_1, char &_2);
	void swap(char *buff, int length);
	void addToData(char *buff, int toAdd);
	void writeImageBytes(const uchar *bytes, int length);
	void synch();
protected:
	bool isLittleEndian;
};

#endif

/*
*/
