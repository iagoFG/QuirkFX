# QuirkFX
QuirkFX allows you develop with JavaFX as quirk as no one has made before.

An image is better than a thousand words, here are some usage examples:

```java
// create a preset with two properties for using later in one or various components
QuirkFX preset1 = QuirkFX.newPreset().setAlign("BASELINE_LEFT").setText("default text");

// create a preset with one property and including other preset for using later
QuirkFX preset2 = QuirkFX.newPreset().setTextAlign("JUSTIFY").setPreset(preset1);

// create a label and set properties in one line
QuirkFX lblgrp = QuirkFX.newLabel().setPreset(preset1).setText("graphic label text");

// change lblgrp text alignment to CENTER
lblgrp.setTextAlign(javafx.scene.text.TextAlignment.CENTER);

// create another label, set properties and use the before created label as graphic
QuirkFX lbl = QuirkFX.newLabel().setPreset(preset2).setText("label text").setGraphic(lblgrp).setTextAlign("JUSTIFY");

// obtain the "warped" original javafx reference
javafx.scene.control.Label lblfx = lbl.get(javafx.scene.control.Label.class);

// create a button with the previous labels as graphics and return directly the warped javafx object
javafx.scene.control.Button btnfx = QuirkFX.newButton().setGraphic(lblfx).get(javafx.scene.control.Button.class);

// get back the graphic property value from lbl
javafx.scene.control.Label lblgrpfx = lbl.get(QuirkFX.Type.GRAPHIC, javafx.scene.control.Label.class);

// get back the lbl text property
String lbltext = lbl.get(QuirkFX.Type.TEXT, String.class);

// get the before javafx button and set a backgroundcolor style and text
QuirkFX.warp(btnfx).setStyle("-fx-background-color:red;").setText("this is a button");
```
