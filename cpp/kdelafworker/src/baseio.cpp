#include <iostream>
#include <math.h>
#include <stdio.h>
#include "baseio.h"


#ifndef min
	#define min(a, b) (a < b) ? a : b
#endif

InBuffer::InBuffer(bool isLittleEndian)
{
	this->isLittleEndian = isLittleEndian;
}

InBuffer::~InBuffer()
{
}

void InBuffer::swap(char &_1, char &_2) {
	char tmp = _1;
	_1 = _2;
	_2 = tmp;
}

void InBuffer::swap(char *buff, int length) {
	int max = (int)ceil((double)length / 2.0);
	for (int i = 0; i < max; i++) {
		swap(buff[i], buff[length - i - 1]);
	}
}

bool InBuffer::read(char *data, int length) {
	return ((unsigned int)length == fread(data, sizeof(char), length, stdin));
}

bool InBuffer::readInt(int32 &val)
{
	int toGet = sizeof(int32);
	char *cVal = (char *)&val;
	read(cVal, toGet);
	if (isLittleEndian)
		swap(cVal, toGet);
	return true;
}

bool InBuffer::readChar(QChar &val)
{
	int toGet = sizeof(ushort);
	ushort uVal;
	char *cVal = (char *)&uVal;
	read(cVal, toGet);
	if (isLittleEndian)
		swap(cVal, toGet);
	val = QChar(uVal);
	return true;
}

bool InBuffer::readByte(int8 &val)
{
	int toGet = sizeof(int8);
	char *cVal = (char *)&val;
	read(cVal, toGet);
	return true;
}

bool InBuffer::readUnsignedByte(uint8 &val)
{
	int toGet = sizeof(uint8);
	char *cVal = (char *)&val;
	read(cVal, toGet);
	return true;
}



bool InBuffer::readBool(bool &val)
{
	int8 iVal;
	if (readByte(iVal)) {
		val = (iVal == 1);
		return true;
	}
	return false;
}

bool InBuffer::readString(QString &val)
{
	bool ok = false;
	int32 len;
	if (ok = readInt(len)) {
		QChar buff[len];
		for (int i = 0; (i < len) && ok; i++) {
			ok = ok && readChar(buff[i]);
		}
		if (ok) {
			val = QString(buff, len);
		}
	}
	return ok;
}

bool InBuffer::readColor(QColor &val)
{
	uint8 red, green, blue; //, alpha;
	bool ok = readUnsignedByte(red);
	ok = ok && readUnsignedByte(green);
	ok = ok && readUnsignedByte(blue);
	//ok = ok && readUnsignedByte(alpha);
	if (ok)
		val.setRgb(red, green, blue); //, alpha/;
	return ok;
}

bool InBuffer::readFont(QFont &val)
{
	QString family;
	int size;
	bool bold, italic;
	bool ok = readString(family);
	ok = ok && readInt(size);
	ok = ok && readBool(bold);
	ok = ok && readBool(italic);
	if (ok) {
		val.setFamily(family);
		val.setPointSize(size);
		val.setBold(bold);
		val.setItalic(italic);
	}
	return ok;
}


OutBuffer::OutBuffer(bool isLittleEndian)
{
	this->isLittleEndian = isLittleEndian;
}

OutBuffer::~OutBuffer()
{
}

void OutBuffer::swap(char &_1, char &_2) {
	char tmp = _1;
	_1 = _2;
	_2 = tmp;
}

void OutBuffer::swap(char *buff, int length) {
	int max = (int)ceil((double)length / 2.0);
	for (int i = 0; i < max; i++) {
		swap(buff[i], buff[length - i - 1]);
	}
}

void OutBuffer::addToData(char *buff, int length)
{
	int numWrite = 0;
	int offset = 0;
	int tmpTotal = length;
	while (tmpTotal > MAX_PACKET_SIZE) {
		numWrite = fwrite(buff+offset, sizeof(char), MAX_PACKET_SIZE, stdout);
		tmpTotal -= numWrite;
		offset += numWrite;
		fflush(stdout);
		synch();
	}
	if (tmpTotal > 0) {
		numWrite = fwrite(buff+offset, sizeof(char), tmpTotal, stdout);
		offset += numWrite;
		fflush(stdout);
	}
}

void OutBuffer::synch()
{
	char c;
	fread(&c, sizeof(char), 1, stdin);
}

bool OutBuffer::send()
{
	return true;
}

void OutBuffer::writeInt(int32 val)
{
	int toWrite = sizeof(int32);
	char *cVal = (char *)&val;
	if (isLittleEndian)
		swap(cVal, toWrite);
	addToData(cVal, toWrite);
}

void OutBuffer::writeChar(const QChar &val)
{
	int toWrite = sizeof(ushort);
	ushort uVal = val.unicode();
	char *cVal = (char *)&uVal;
	if (isLittleEndian)
		swap(cVal, toWrite);
	addToData(cVal, toWrite);
}

void OutBuffer::writeByte(int8 val)
{
	int toWrite = sizeof(int8);
	char *cVal = (char *)&val;
	addToData(cVal, toWrite);
}

void OutBuffer::writeUnsignedByte(uint8 val)
{
	int toWrite = sizeof(uint8);
	char *cVal = (char *)&val;
	addToData(cVal, toWrite);
}

void OutBuffer::writeBool(bool val)
{
	int8 iVal = val ? 1 : 0;
	writeByte(iVal);
}

void OutBuffer::writeString(const QString &val)
{
	int len = val.length();
	writeInt(len);
	for (int i = 0; i < len; i++) {
		writeChar(val.at(i));
	}
}

void OutBuffer::writeColor(const QColor &val)
{
	writeUnsignedByte((uint8)val.red());
	writeUnsignedByte((uint8)val.green());
	writeUnsignedByte((uint8)val.blue());
}

void OutBuffer::writeFont(const QFont &val)
{
	writeString(val.family());
	writeInt(val.pointSize());
	writeBool(val.bold());
	writeBool(val.italic());
}

void OutBuffer::writeImageBytes(const uchar *bytes, int length)
{
	int toSend = sizeof(uchar) * length;
	if (isLittleEndian) {
		char *buff = (char *)malloc(sizeof(char) * toSend);
		buff = (char *)memcpy(buff, bytes, sizeof(char) * toSend);
		for (int i = 0; i < toSend; i+=4)
			swap(buff + i, 4);
		addToData(buff, toSend);
		free(buff);
	}
	else
		addToData((char *)bytes, toSend);
}
	

void OutBuffer::writeStrings(QStringList &val)
{
	int32 size = val.size();
	writeInt(size);
	for (QStringList::Iterator it = val.begin(); it != val.end(); ++it)
		writeString(*it);
}

void OutBuffer::writeRect(const QRect &val)
{
	writeInt(val.x());
	writeInt(val.y());
	writeInt(val.width());
	writeInt(val.height());
}

void OutBuffer::writeImage(const QImage &val)
{
	int width = val.width();
	int height = val.height();
	int numBytes = width * height * 4;

	writeInt(width);
	writeInt(height);
	writeImageBytes(val.bits(), numBytes);
}

void OutBuffer::writeImage(const QImage &val, const QColor &transparentColor) {
	QImage valAlpha(val);
	valAlpha.setAlphaBuffer(true);
	int width = valAlpha.width();
	int height = valAlpha.height();
	int transpPixel = transparentColor.rgb();
	for (int x = 0; x < width; x++) {
		for (int y = 0; y < height; y++) {
			QRgb pixel = val.pixel(x, y);
			if (pixel == (QRgb)transpPixel)
				valAlpha.setPixel(x, y, qRgba(qRed(pixel), qGreen(pixel), qBlue(pixel), 0));
		}
	}
	writeImage(valAlpha);
}
