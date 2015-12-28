package chustiboy;

import chustiboy.net.Network;
import chustiboy.net.packets.Packet_color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class Player extends VisTable {

	public String playerID;
	private String name;
	public byte connectionID;
	private Texture color_texture;
	private Pixmap pix;
	private VisImage i;
	private VisTextButton i_button;
	private final ColorPicker picker;
	private Label name_label, listo;
	public float r, g, b;
	private boolean ready;

	public Player(final Network net) {
		pix = new Pixmap(16, 16, Pixmap.Format.RGB888);
		i = new VisImage();
		set_img_texture();
		name_label = new VisLabel();
		listo = new VisLabel();
		ready = false;

		picker = new ColorPicker(new ColorPickerAdapter() {
			@Override
			public void finished(Color newColor) {
				r = newColor.r;
				g = newColor.g;
				b = newColor.b;

				set_img_texture();
				
				Packet_color p = new Packet_color();
				p.pj_id = connectionID;
				p.r = r;
				p.g = g;
				p.b = b;
				net.sendTCP(p);
			}
		});
		picker.setAllowAlphaEdit(false);

		i_button = new VisTextButton("");
		i_button.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				getStage().addActor(picker.fadeIn());
			}
		});
		i_button.add(i);
		i_button.setDisabled(true);

		add(listo).width(30).padRight(10);
		add(i_button);
		add(name_label).width(300).padLeft(10);
	}

	public void set_img_texture(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.set_img_texture();
	}
	
	private void set_img_texture() {

		pix.setColor(r, g, b, 1);
		pix.fill();

		color_texture = new Texture(pix);
		i.setDrawable(color_texture);
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
		
		if(ready) {
			listo.setText("listo");
		}
		else {
			listo.setText("");
		}
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void set_mine() {
		i_button.setDisabled(false);
	}
	
	public void setName(String name) {
		this.name = name;
		this.name_label.setText(name);
	}
	
	public String getName() {
		return name;
	}
		
	public void dispose() {
		color_texture.dispose();
		pix.dispose();
		picker.dispose();
	}
}
