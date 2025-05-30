package javafx.scene;

import java.util.Collection;
import java.util.LinkedList;

/// QuirkFX allows you develop with JavaFX as quirk as no one has made before.
/// (c) 2021 iagoFG.com, licensed under MPL-2.0
/// This library is still under development. Be kind, check some examples at the bottom...
public class QuirkFX {
	
	public enum Type {
		PRESET, STYLE, TEXT, GRAPHIC, ALIGN, TEXTALIGN;
	}
	
	public final static String ERROR_WRONG_ENUM = "WRONG_ENUM A parameter not corresponding to the enum values was passed, String to Enum conversion is not possible";
	
	public static abstract class Handler {
		public abstract boolean handle(Object handlee);
	}
	
	private static Handler onerror = new Handler() {
		public boolean handle(Object handlee) {
			System.err.println(String.valueOf(handlee));
			return true;
		}
	};
	
	private class Entry {
		private Type type;
		private Object[] props;
		private Entry(Type type, Object[] props) {
			this.type = type;
			this.props = props;
		}
	}
	private Node warped = null;
	private Collection<Entry> presets = null;
	
	private QuirkFX() {} // forbidden to create directly instances, factories must be used
	
	/// FACTORIES
	
	public static QuirkFX newPreset() {
		QuirkFX q = new QuirkFX();
		q.presets = new LinkedList<>();
		return q;
	}
	
	public static QuirkFX newLabel() {
		QuirkFX q = new QuirkFX();
		q.warped = new javafx.scene.control.Label();
		return q;
	}
	//public static QuirkFX newLabel(String text) { return newLabel().setText(text); } // now it's easy! c'mon!! use base factory and two setters instead
	//public static QuirkFX newLabel(String text, Node graphic) { return newLabel().setText(text).setGraphic(graphic); }
	
	public static QuirkFX newButton() {
		QuirkFX q = new QuirkFX();
		q.warped = new javafx.scene.control.Button();
		return q;
	}
	
	public static QuirkFX warp(Node towarp) {
		QuirkFX q = new QuirkFX();
		q.warped = towarp;
		return q;
	}
	
	/// PRESET AND GENERIC PROPERTIES MANAGEMENT
	
	public static void setErrorHandler(Handler onerror) {
		QuirkFX.onerror = onerror;
	}
	
	public QuirkFX setPreset(QuirkFX preset) {
		if (presets != null) presets.add(new Entry(Type.PRESET, new Object[] { preset })); // if this is a preset, include the given preset
		else if (warped != null && preset != null && preset.presets != null) for (Entry e: preset.presets) applyEntry(e); // otherwise if this is a warped, then apply the given preset to the warped component
		return this;
	}
	private void applyEntry(Entry entry) {
		set(entry.type, entry.props);
	}
	
	public QuirkFX set(Type type, Object... properties) {
		switch (type) {
		case STYLE: this.setStyle((String)properties[0]);
		case PRESET: this.setPreset((QuirkFX)properties[0]);
		case TEXT: this.setText((String)properties[0]);
		case GRAPHIC: this.setGraphic((Node)properties[0]);
		case ALIGN: this.setAlign((javafx.geometry.Pos)properties[0]);
		case TEXTALIGN: this.setTextAlign((javafx.scene.text.TextAlignment)properties[0]);
		}
		return this;
	}
	public Object get(Type propertyType) {
		Object res = null;
		if (propertyType != null) {
			if (presets != null) for (Entry e: presets) if (propertyType.equals(e.type)) res = (String)e.props[0];
			if (warped != null) {
				switch (propertyType) {
				case STYLE: if (warped instanceof Node) res = ((Node)warped).getStyle();
				case TEXT: if (warped instanceof javafx.scene.control.Labeled) res = ((javafx.scene.control.Labeled)warped).getText();
				case GRAPHIC: if (warped instanceof javafx.scene.control.Labeled) res = ((javafx.scene.control.Labeled)warped).getGraphic();
				case ALIGN: if (warped instanceof javafx.scene.control.Labeled) res = ((javafx.scene.control.Labeled)warped).getAlignment();
				case TEXTALIGN: if (warped instanceof javafx.scene.control.Labeled) res = ((javafx.scene.control.Labeled)warped).getTextAlignment();
				}
			}
		}
		return res;
	}
	@SuppressWarnings("unchecked")
	public <T> T get(Type propertyType, Class<T> clazz) {
		Object propertyValue = get(propertyType);
		if (propertyValue != null && clazz != null && clazz.isAssignableFrom(propertyValue.getClass())) return (T)propertyValue;
		return null;
	}
	
	public Node get() {
		return warped;
	}
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) {
		if (warped != null && clazz != null && clazz.isAssignableFrom(warped.getClass())) return (T)warped;
		return null;
	}
	
//	public <T> T mapEnum(String enumValue, Class<T> clazz) {
//		for (T e: clazz.values()) if (e.toString().equals(enumValue)) return e;
//		if (onerror != null && onerror.handle(ERROR_WRONG_ENUM));
//		return null;
//	}
	
	/// PROPERTY SETTERS
	
	public QuirkFX setStyle(String value) {
		if (warped != null && warped instanceof Node) ((Node)warped).setStyle(value);
		if (presets != null) presets.add(new Entry(Type.STYLE, new Object[] { value }));
		return this;
	}
	
	public QuirkFX setText(String value) {
		if (warped != null && warped instanceof javafx.scene.control.Labeled) ((javafx.scene.control.Labeled)warped).setText(value);
		if (presets != null) presets.add(new Entry(Type.TEXT, new Object[] { value }));
		return this;
	}
	
	public QuirkFX setGraphic(Node value) {
		if (warped != null && warped instanceof javafx.scene.control.Labeled) ((javafx.scene.control.Labeled)warped).setGraphic(value);
		if (presets != null) presets.add(new Entry(Type.GRAPHIC, new Object[] { value }));
		return this;
	}
	public QuirkFX setGraphic(QuirkFX value) {
		return setGraphic(value.get());
	}
	
	public QuirkFX setAlign(javafx.geometry.Pos value) {
		if (warped != null && warped instanceof javafx.scene.control.Labeled) ((javafx.scene.control.Labeled)warped).setAlignment(value);
		if (presets != null) presets.add(new Entry(Type.ALIGN, new Object[] { value }));
		return this;
	}
	public QuirkFX setAlign(String pos) {
		for (javafx.geometry.Pos e: javafx.geometry.Pos.values()) if (e.toString().equals(pos)) return setAlign(e);
		if (onerror != null && onerror.handle(ERROR_WRONG_ENUM)) return this; else return null;
	}
	
	public QuirkFX setTextAlign(javafx.scene.text.TextAlignment value) {
		if (warped != null && warped instanceof javafx.scene.control.Labeled) ((javafx.scene.control.Labeled)warped).setTextAlignment(value);
		if (presets != null) presets.add(new Entry(Type.TEXTALIGN, new Object[] { value }));
		return this;
	}
	public QuirkFX setTextAlign(String textAlignment) {
		for (javafx.scene.text.TextAlignment e: javafx.scene.text.TextAlignment.values()) if (e.toString().equals(textAlignment)) return setTextAlign(e);
		if (onerror != null && onerror.handle(ERROR_WRONG_ENUM)) return this; else return null;
	}
	
	/// PROPERTY GETTERS
	
	public String getStyle() {
		if (warped != null) return ((Node)warped).getStyle(); else return (String)get(Type.STYLE);
	}
	
	public String getText() {
		if (warped != null) return ((javafx.scene.control.Labeled)warped).getText(); else return (String)get(Type.TEXT);
	}
	
	public Node getGraphic() {
		if (warped != null) return ((javafx.scene.control.Labeled)warped).getGraphic(); else return (Node)get(Type.GRAPHIC);
	}
	
	public javafx.geometry.Pos getAlign() {
		if (warped != null) return ((javafx.scene.control.Labeled)warped).getAlignment(); else return (javafx.geometry.Pos)get(Type.ALIGN);
	}
	
	public javafx.scene.text.TextAlignment getTextAlign() {
		if (warped != null) return ((javafx.scene.control.Labeled)warped).getTextAlignment(); else return (javafx.scene.text.TextAlignment)get(Type.TEXTALIGN);
	}
	
	/// EXAMPLES
	/// here are some basic examples and possibilities with this API in the current version
	public static void examples() {
		
		// create a preset with two properties for using later
		QuirkFX preset1 = QuirkFX.newPreset().setAlign("BASELINE_LEFT").setText("default text");
		
		// create a preset with one property and including other preset for using later
		QuirkFX preset2 = QuirkFX.newPreset().setTextAlign("JUSTIFY").setPreset(preset1);
		
		// create a label and set properties in one line
		QuirkFX lblgrp = QuirkFX.newLabel().setPreset(preset1).setText("graphic label text");
		
		// change lblgrp text alignment to CENTER
		lblgrp.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
		
		// create another label, set properties and use the before created label as graphic
		QuirkFX lbl = QuirkFX.newLabel().setPreset(preset2).setText("label text")
				.setGraphic(lblgrp).setTextAlign("JUSTIFY");
		
		// obtain the "warped" original javafx reference
		javafx.scene.control.Label lblfx = lbl.get(javafx.scene.control.Label.class);
		
		// create a button with the previous labels as graphics and return directly the warped javafx object
		javafx.scene.control.Button btnfx = QuirkFX.newButton()
				.setGraphic(lblfx).get(javafx.scene.control.Button.class);
		
		// get back the graphic property value from lbl
		javafx.scene.control.Label lblgrpfx = lbl.get(QuirkFX.Type.GRAPHIC, javafx.scene.control.Label.class);
		
		// get back the lbl text property
		String lbltext = lbl.get(QuirkFX.Type.TEXT, String.class);
		
		// get the before javafx button and set a backgroundcolor style and text
		QuirkFX.warp(btnfx).setStyle("-fx-background-color:red;").setText("this is a button");
		
	}
	
}
