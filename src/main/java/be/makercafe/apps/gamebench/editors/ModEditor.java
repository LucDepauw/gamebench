/*
    Copyright 2015 - 2016, Luc De pauw - Makercafe.be
    This file is part of Makerbench.

    Makerbench is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Makerbench is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Makerbench.  If not, see <http://www.gnu.org/licenses/>.
*/
package be.makercafe.apps.gamebench.editors;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import be.makercafe.apps.gamebench.ResourceManager;
import be.makercafe.apps.gamebench.model.GameCartridge;
import be.makercafe.apps.gamebench.model.TextureDTO;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.util.Callback;

@SuppressWarnings("restriction")
public class ModEditor extends Editor {

	private final Group viewGroup;

	private LevelCanvas levelCanvas;

	private ScrollPane editorContainer;

	private ToolBar toolBar = null;

	private ComboBox<TextureItem> cbxTextures = null;

	private GameCartridge gameCartridge = new GameCartridge();

	private List<Image> textures = new ArrayList<Image>();

	private int[][] map;

	public ModEditor(String tabText, Path path) {
		super(tabText);

		this.viewGroup = new Group();
		this.editorContainer = new ScrollPane();
		this.levelCanvas = new LevelCanvas(32 * 24, 32 * 24, this);

		this.editorContainer.setContent(this.levelCanvas.getCanv());

		BorderPane rootPane = new BorderPane();

		toolBar = createToolBar();

		rootPane.setTop(toolBar);
		rootPane.setCenter(editorContainer);
		this.getTab().setContent(rootPane);
		loadCartridge(path);
		displayMap();

	}

	private void loadCartridge(Path path) {
		this.gameCartridge = ResourceManager.loadCartridge(path.toString());
		List<TextureDTO> texturesDTO = this.gameCartridge.getLevels().get(0).getTextures();
		this.cbxTextures.getItems().clear();
		this.textures.clear();
		int index = 0;
		try {
			File workspace = path.getParent().toFile();
			File assets = new File(workspace.getPath() + File.separatorChar + "assets");
			for (TextureDTO textureDTO : texturesDTO) {
				this.textures
						.add(new Image(new File(assets.getPath() + textureDTO.getPath()).toURI().toURL().toString()));
				this.cbxTextures.getItems().add(new TextureItem(index, textureDTO.getPath(), textureDTO.getPath()));
				index++;
			}
			this.map = this.gameCartridge.getLevels().get(0).getMap();
		} catch (Exception ex) {
			//TODO: fix me
			ex.printStackTrace();
		}

	}

	private void displayMap() {
		int size = 32;
		for (int y = 0; y < 24; y++) {
			for (int x = 0; x < 24; x++) {
				if (this.map[x][y] > 0) {
					try {
					this.levelCanvas.getCanvasContext().drawImage(this.textures.get(this.map[x][y] - 1), x * size,
							y * size, size, size);
					}catch(IndexOutOfBoundsException ex) {
						//TODO: fix me
						ex.printStackTrace();
					}
				}
			}
		}
	}

	private ToolBar createToolBar() {

		ToolBar toolBar = new ToolBar();
		toolBar.setOrientation(Orientation.HORIZONTAL);

		Button btnSave = GlyphsDude.createIconButton(MaterialDesignIcon.FLOPPY, "Save");
		btnSave.setOnAction(this::handleSaveButton);

		Button btnExportSTL = GlyphsDude.createIconButton(MaterialDesignIcon.EXPORT, "Export mod");

		btnExportSTL.setOnAction(this::handleExportAsMod);

		Button btnExportPNG = GlyphsDude.createIconButton(MaterialDesignIcon.CAMERA, "Screenshot map");
		btnExportPNG.setOnAction(this::handleExportAsScreenshot);

		Button btnRun = GlyphsDude.createIconButton(MaterialDesignIcon.DRAWING, "Draw");
		btnRun.setOnAction(this::handleDraw);

		Button btnAutoCompile = GlyphsDude.createIconButton(MaterialDesignIcon.ERASER, "Erase");
		btnAutoCompile.setOnAction(this::handleErase);

		Button btnAddTexture = GlyphsDude.createIconButton(MaterialDesignIcon.FILE_IMAGE, "Add texture");
		btnAddTexture.setOnAction(this::handleAddTexture);

		ComboBox<TextureItem> cbxSourceExamples = new ComboBox<TextureItem>();
		Callback<ListView<TextureItem>, ListCell<TextureItem>> factory = lv -> new ListCell<TextureItem>() {

			@Override
			protected void updateItem(TextureItem item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}

		};

		cbxSourceExamples.setCellFactory(factory);
		cbxSourceExamples.setButtonCell(factory.call(null));
		this.cbxTextures = cbxSourceExamples;

		toolBar.getItems().addAll(btnSave, btnExportSTL, btnExportPNG, new Separator(), btnRun, new Separator(),
				btnAutoCompile, new Separator(), cbxSourceExamples, new Separator(), btnAddTexture);
		return toolBar;

	}

	private void handleSaveButton(ActionEvent event) {
		if (!saveJsonMod()) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unable to save file.");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Oeps an error occured");
			alert.setHeaderText("Cannot save file. There went something wrong writing the file.");
			alert.setContentText(
					"Please verify that your file is not read only, is not locked by other user or program, you have enough diskspace.");
			alert.showAndWait();
		}
	}

	private boolean saveJsonMod() {
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) this.getTab().getUserData();
		String path = map.get("path");
		prepareJsonFile();
		return ResourceManager.saveCartridge(path, this.gameCartridge);

	}

	private void prepareJsonFile() {
		// fill in currentmap
		this.gameCartridge.getLevels().get(0).setMap(this.map);
		// save/update texture data
		this.gameCartridge.getLevels().get(0).getTextures().clear();
		for(TextureItem ti :  this.cbxTextures.getItems()) {
			this.gameCartridge.getLevels().get(0).getTextures().add(new TextureDTO(ti.getPath(),64));
		}
		
	}

	private void handleExportAsMod(ActionEvent e) {

		if (!saveJsonMod()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Oeps an error occured");
			alert.setHeaderText("Cannot export mod.");
			alert.setContentText("An error occured when saving the json file.");
			alert.showAndWait();
			return;
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export mod File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Zip files (*.zip)", "(*.zip)")); // 2x
																												// bug
																												// in
																												// code

		File f = fileChooser.showSaveDialog(null);

		if (f == null) {
			return;
		}

		String fName = f.getAbsolutePath();

		if (!fName.toLowerCase().endsWith(".zip")) {
			fName += ".zip";
		}

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) this.getTab().getUserData();
		String path = map.get("path");
		try {

			File mod = new File(path);

			File workspace = mod.getParentFile();
			ZipTools.zipFile(workspace, fName);
		} catch (Exception ex) {

			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Oeps an error occured");
			alert.setHeaderText("Cannot export gcode. There went something wrong writing the file.");
			alert.setContentText(
					"Please verify that your file is not read only, is not locked by other user or program, you have enough diskspace.");
			alert.showAndWait();
		}
	}

	private void handleExportAsScreenshot(ActionEvent e) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export PNG File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files (*.png)", "*.png"));

		File f = fileChooser.showSaveDialog(null);

		if (f == null) {
			return;
		}

		String fName = f.getAbsolutePath();

		if (!fName.toLowerCase().endsWith(".png")) {
			fName += ".png";
		}

		SnapshotParameters snapshotParameters = new SnapshotParameters();
		snapshotParameters.setFill(Color.TRANSPARENT);

		try {
			WritableImage wi = new WritableImage((int) 32 * 24, (int) 32 * 24);
			ImageIO.write(SwingFXUtils.fromFXImage(this.levelCanvas.getCanv().snapshot(null, wi), null), "png",
					new File(fName));
		} catch (IOException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Oeps an error occured");
			alert.setHeaderText("Cannot export PNG. There went something wrong writing the file.");
			alert.setContentText(
					"Please verify that your file is not read only, is not locked by other user or program, you have enough diskspace.");
			alert.showAndWait();
		}
	}

	private void handleDraw(ActionEvent e) {
		this.levelCanvas.setActiveAction(LevelCanvas.ACTION_DRAW_PEN);
	}

	private void handleErase(ActionEvent e) {
		this.levelCanvas.setActiveAction(LevelCanvas.ACTION_DRAW_ERASE);
	}

	private void handleAddTexture(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		String path = getProjectPath();
		try {

			File mod = new File(path);

			File workspace = mod.getParentFile();

			File assets = new File(workspace.getPath() + File.separatorChar + "assets");
			Logger.getLogger(this.getClass().getName()).log(Level.INFO,
					workspace.getPath() + File.separatorChar + "assets");

			fileChooser.setInitialDirectory(assets);
			fileChooser.setTitle("Add texture");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files (*.png)", "*.png"));

			File f = fileChooser.showOpenDialog(null);

			if (f == null) {
				return;
			}

			String fName = f.getAbsolutePath();

			if (!fName.toLowerCase().endsWith(".png")) {
				fName += ".png";
			}
			Logger.getLogger(this.getClass().getName()).log(Level.INFO,
					fName + ":" + f.getPath() + ":" + f.toURI().toURL().toString());

			this.textures.add(new Image(f.toURI().toURL().toString()));
			int index = this.cbxTextures.getItems().size();
			String filePath = f.getPath().substring(assets.getPath().length());
			this.cbxTextures.getItems().add(new TextureItem(index, filePath, filePath));

		} catch (Exception ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Oeps an error occured");
			alert.setHeaderText("Cannot add texture. Something wrong reading the file.");
			alert.setContentText("Please check that file is a valid PNG bitmap format.");
			alert.showAndWait();
		}

	}

	private String getProjectPath() {
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) this.getTab().getUserData();
		String path = map.get("path");
		return path;
	}

	public ToolBar getToolBar() {
		return toolBar;
	}

	public void setToolbar(ToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public LevelCanvas getLevelCanvas() {
		return levelCanvas;
	}

	public void setLevelCanvas(LevelCanvas levelCanvas) {
		this.levelCanvas = levelCanvas;
	}

	public ComboBox<TextureItem> getCbxTextures() {
		return cbxTextures;
	}

	public void setCbxTextures(ComboBox<TextureItem> cbxTextures) {
		this.cbxTextures = cbxTextures;
	}

	public GameCartridge getGameCartridge() {
		return gameCartridge;
	}

	public void setGameCartridge(GameCartridge gameCartridge) {
		this.gameCartridge = gameCartridge;
	}

	public List<Image> getTextures() {
		return textures;
	}

	public void setTextures(List<Image> textures) {
		this.textures = textures;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

}
