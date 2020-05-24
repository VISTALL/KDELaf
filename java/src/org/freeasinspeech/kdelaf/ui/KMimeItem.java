package org.freeasinspeech.kdelaf.ui;
/*
 * @author Sekou DIAKITE (diakite_at_freeasinspeech_dot_org)
 */
public class KMimeItem
{
	protected String[] extensions;
	protected String comment;
	protected String localeComment;
	protected String mimeType;
	protected String icon;

	public KMimeItem() {
		extensions = null;
		comment = null;
		localeComment = null;
		mimeType = null;
		icon = null;
	}
	
	public KMimeItem(String []extensions, String comment, String localeComment, String mimeType, String icon) {
		this.extensions = Utilities.copyStrArray(extensions);
		this.comment = (comment != null) ? new String(comment) : "";
		this.localeComment = (localeComment != null) ? new String(localeComment) : "";
		this.mimeType = (mimeType != null) ? new String(mimeType) : "";
		this.icon = (icon != null) ? new String(icon) : "unknown";
	}
	
	public KMimeItem(String extensions, String comment, String localeComment, String mimeType, String icon) {
		this.extensions = new String[1];
		this.extensions[0] = new String((extensions != null) ? new String(extensions) : "???");
		this.comment = (comment != null) ? new String(comment) : "";
		this.localeComment = (localeComment != null) ? new String(localeComment) : "";
		this.mimeType = (mimeType != null) ? new String(mimeType) : "";
		this.icon = (icon != null) ? new String(icon) : "unknown";
	}
	
	public KMimeItem(KMimeItem item) {
		this.extensions = Utilities.copyStrArray(item.extensions);
		this.comment = (item.comment != null) ? new String(item.comment) : "";
		this.localeComment = (item.localeComment != null) ? new String(item.localeComment) : "";
		this.mimeType = (item.mimeType != null) ? new String(item.mimeType) : "";
		this.icon = (item.icon != null) ? new String(item.icon) : "unknown";
	}
	
	public String []getExtensions() {
		return Utilities.copyStrArray(extensions);
	}
	public String getComment() {
		return new String(comment);
	}
	public String getLocaleComment() {
		return new String(localeComment);
	}
	public String getMimeType() {
		return new String(mimeType);
	}
	public String getIconName() {
		return new String(icon);
	}
	public void setIconName(String icon) {
		this.icon = (icon != null) ? new String(icon) : "unknown";
	}
	
	
	public String toString() {
		String patterns = "";
		if (extensions != null)
			for (int i = 0; i < extensions.length; i++)
				patterns = "*." + extensions[i] + ";";
		return "MimeType="+mimeType+":Icon="+icon+":Patterns="+patterns+":Comment="+comment+":Comment["+KLocale.getLanguage()+"]="+localeComment;
	}
	
	public static KMimeItem unknownItem() {
		return new KMimeItem("???", "unknown", "unknown", "unknown", "unknown");
	}
}
