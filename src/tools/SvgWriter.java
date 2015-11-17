package tools;

import java.io.IOException;
import java.io.Writer;

import fr.enseirb.battleship.Grid;
import fr.enseirb.battleship.Ship;

// http://www.labri.fr/perso/falleri/dist/ens/pg220/tps/tp2/tp2_src.zip

public class SvgWriter {

	private int width;
	private int height;

	private int cell;
	private int stroke;
	private int font_size;
	
	private int img_height;
	private int img_width;
	
	private static final String line = "<line x1='%d' y1='%d' x2='%d' y2='%d' style='stroke:rgb(0,0,0);stroke-width:%d' />\n";
	private static final String text = "<text x='%d' y='%d' alignment-baseline='central' text-anchor='middle' font-size='%d'>%d</text>\n";
	private static final String boat = "<rect x='%d' y='%d' width='%d' height='%d' style='fill:rgb(0,0,0);fill-opacity:0.5;' />\n";

	public SvgWriter(int width, int height) {
		
		this.width = width;
		this.height = height;
		this.cell = Config.CELL_DIM;
		this.stroke = Config.STROKE_WIDTH;
		this.font_size = this.cell / 2;
		
		this.img_height = (height+1) * cell;
		this.img_width = (width+1) * cell;
	};

	public void createGrid(Writer w) throws IOException {
		
		w.append(String.format("<svg xmlns='http://www.w3.org/2000/svg' version='1.1' width='%d' height='%d'>\n",
				img_width, img_height));

		// Container
		w.append(String.format("<rect x='%d' y='%d' width='%d' height='%d' style='fill:rgb(255,255,255);stroke-width:%d;stroke:rgb(0,0,0)' />\n", 0, 0, img_width, img_height, stroke));
		
		// Separating index 
		w.append(String.format(SvgWriter.line, cell, 0, cell, img_height, stroke*2));
		w.append(String.format(SvgWriter.line, 0, cell, img_width, cell, stroke*2));
		
		for (int i = 2; i <= width; i++) {
			// Vertical
			w.append(String.format(SvgWriter.line, i*cell, 0, i*cell, img_height, stroke));
			// Horizontal
			w.append(String.format(SvgWriter.line, 0, i*cell, img_width, i*cell, stroke));
		}
		
		// Numbering
		for (int i = 0; i < width; i++) {
			w.append(String.format(SvgWriter.text,
					(i+1)*cell + cell/2, // x
					cell - font_size/2, // y
					font_size, // Font size
					i));
			w.append(String.format(SvgWriter.text,
					cell/2,
					(i+2)*cell - font_size/2,
					font_size,
					i));
		}
	}
	
	public void debugGrid(Writer w, Grid grid) {
		
		try {
			w.append("<?xml version='1.0' encoding='utf-8'?>\n");
			
			this.createGrid(w);
			
			// Boats
			for (Ship ship : grid.getShips()) {
				
				if (ship.isHorizontal())
					w.append(String.format(SvgWriter.boat,
										(ship.getX()+1)*cell, // Adding one, as there is numbering
										(ship.getY()+1)*cell,
										ship.getSize()*cell,
										cell));
				else
					w.append(String.format(SvgWriter.boat,
							(ship.getX()+1)*cell,
							(ship.getY()+1)*cell,
							cell,
							ship.getSize()*cell));
			}
			
			w.append("</svg>");
			w.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}