import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import canvas.Canvas;




public class DessinArbre extends JComponent {
	

	private static ListListener nameList;
	Arbre arbre;
	Arbre.Tree tree[];
	
	int current;
	private static Explorer exp;
	private static DessinArbre dessin;
	
		
	public DessinArbre(Container contentPane){
		arbre = new Arbre();
		tree = new Arbre.Tree[3];
		current = 0;
		tree[current] = arbre.create();

		setPreferredSize(new Dimension(800,400));
		setMinimumSize(new Dimension(800,400));


		
		Box panel = Box.createVerticalBox();
		
		//panel.setBorder(BorderFactory.createTitledBorder("Selection"));
		
		JButton button = new JButton("Suivant");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						current = (current+1)%3;
						DessinArbre.this.repaint();
						refreshCanvas();
					}					
				}
		);
		button.setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));
		panel.add(button);
		button = new JButton("Précédent");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						current = (current+2)%3;
						DessinArbre.this.repaint();
						refreshCanvas();
					}					
				}
		);
		button.setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));

		panel.add(button);
		button = new JButton("Créer");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						tree[current]= arbre.create();
						DessinArbre.this.repaint();
						refreshCanvas();
					}					
				}
		);
		button.setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));

		panel.add(button);
		button = new JButton("Effacer");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						tree[current]= null;
						DessinArbre.this.repaint();
						refreshCanvas();
					}					
				}
		);
		button.setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));

		panel.add(button);
		button = new JButton("Pousser");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						if (tree[current] != null){
							arbre.bloom(tree[current]);
							DessinArbre.this.repaint();
							refreshCanvas();
						}
					}					
				}
		);
		button.setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));

		panel.add(button);
		button = new JButton("Agrandir");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						if (tree[current] != null){
							arbre.enlarge(tree[current]);
							DessinArbre.this.repaint();
							refreshCanvas();
						}
					}					
				}
		);
		button.setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));

		panel.add(button);
		button = new JButton("Rétrécir");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						if (tree[current] != null){
							arbre.reduce(tree[current]);
							DessinArbre.this.repaint();
							refreshCanvas();
						}
					}					
				}
		);
		button.setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));

		panel.add(button);
		button = new JButton("Copie");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						int empty = -1;
						for(int i = 0; (i < 3) && (empty == -1); ++i){
							if (tree[i]== null) empty = i;
						}
						if ((tree[current] != null)&&(empty != -1)){
							tree[empty]=arbre.copy(tree[current]);
							DessinArbre.this.repaint();
						}
						refreshCanvas();
					}					
				}
		);
		button.setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));

		panel.add(button);
		
		button = new JButton("Colorier");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {

						if (tree[current] != null){
							arbre.color(tree[current]);
							DessinArbre.this.repaint();
							refreshCanvas();
						}
					}					
				}
		);
		button.setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));

		panel.add(button);
		
//		contentPane.add(this);
		Box hBox = Box.createHorizontalBox();
		hBox.add(this);
		hBox.add(panel);

		contentPane.add(hBox);
	
	}
	
	public void paintArbre(Graphics2D g,
						    Arbre.Tree a, 
						   double x,double y,
						   double angle){
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		Polygon poly = new Polygon();
		Shape shape = new Rectangle(0,-a.thickness/2,a.length,a.thickness);
		angle = angle+a.angle;
		//AffineTransform t = AffineTransform.getRotateInstance(angle,0,-a.thickness/2.0);
	    //
		AffineTransform t = AffineTransform.getTranslateInstance(x, y);
		t.concatenate(AffineTransform.getRotateInstance(-angle));
	    
	    
	    Point2D p = new Point2D.Double(a.length,0);
	    p=t.transform(p,p);
	    
	    if (a.left != null){
	    	paintArbre(g,a.left,p.getX(),p.getY(),angle);
	    }

	    if (a.right != null){
	    	paintArbre(g,a.right,p.getX(),p.getY(),angle);
	    }
	    
//	    if (a.right == null && a.left == null){
//	    	g.setColor(Color.GREEN);
//	    	g.fill(t.createTransformedShape(new Ellipse2D.Double(-a.length,-a.length,2*a.length,2*a.length)));
//	    }
	    g.setColor(new Color(a.color));
	    g.fill(t.createTransformedShape(shape));
	    
	
	}	

	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		for(int i = 0; i < 3; ++i){
			if (tree[i] != null)
				paintArbre(g2d,tree[i],100+250*i,350,Math.PI/2.0);
			if (i==current){
				g2d.setColor(Color.RED);
				g2d.fill(new Rectangle(80+250*i,370,40,10));
			}
		}
	}

	private void refreshCanvas(){
		exp.read(tree[0],"tree[0]");
		exp.read(tree[1],"tree[1]");
		exp.read(tree[2],"tree[2]");
		exp.read(tree[current],"current");
		exp.refresh();
		nameList.update();
		repaint();
	}
	
	
	static void common() {
			JFrame mainFrame = new JFrame("Arbres");		
//			Container contentPane = mainFrame.getContentPane();
			
//			contentPane.setLayout(new FlowLayout());
			Box vbox = Box.createVerticalBox();
			
			dessin = new DessinArbre(vbox);
			
			final Canvas canvas = new Canvas(800, 300);
			exp = new Explorer(canvas);
		
			JScrollPane scrollPane = new JScrollPane(   canvas);
	
					scrollPane.setSize(800, 300);
			
			Box hbox = Box.createHorizontalBox();
			JButton button = new JButton("Zoom +");
			button.addActionListener(new AbstractAction(){
	
				public void actionPerformed(ActionEvent arg0) {
					canvas.multScale(1.2);				
				}
			}
			);
			hbox.add(button);
			button = new JButton("Normal");
			button.addActionListener(new AbstractAction(){
	
				public void actionPerformed(ActionEvent arg0) {
					canvas.setScale(1);				
				}
			}
			);
			hbox.add(button);
	
			button = new JButton("Zoom -");
			button.addActionListener(new AbstractAction(){
	
				public void actionPerformed(ActionEvent arg0) {
					canvas.multScale(0.8);				
				}
			}
			);
			hbox.add(button);
			
			hbox.add(Box.createGlue());
			
			hbox.add(new JLabel("Sélection "));
			
	
			nameList = new ListListener(exp);
//			canvas.set
			canvas.addSelectionListener(nameList);
			hbox.add(nameList);
	
			vbox.add(hbox);

	/*		scrollPane.setBackground(Color.lightGray);
			canvas.setBackground(Color.lightGray);
			*/
	
			scrollPane.setBorder(BorderFactory.createTitledBorder("Mémoire"));
			scrollPane.setOpaque(false);
			scrollPane.getViewport().setOpaque(false);
	
			vbox.add(scrollPane);
			
			mainFrame.add(vbox);
	
			
			dessin.refreshCanvas();
			//mainFrame.setMinimumSize(new Dimension(800,800));
			mainFrame.pack();
			mainFrame.setVisible(true);
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		}	
}
