import java.awt.Component;




public class Arbre {

	
	class Tree {
		int length; //longueur du segment
		int thickness; // épaisseur du segment
		double angle; // angle du segment (par rapport à celui du segment père
		
		int color; // couleur du segment 0xRRVVBB
		
		Tree left;  // segment suivant (le plus à gauche)
		Tree right; // segment suivant (le plus à droite)
	}
	
	Tree create(){
		// Cette fonction crée un arbre (noir) composé d'un tronc
		// vertical et deux branches légèrement inclinées.
		// Essayez de modifier ces valeurs et observez les effets
		
		
		Tree result = new Tree(); // Création du tronc
		
		// définition des attributs du tronc
		result.length = 100;
		result.thickness = 10;
		result.angle = 0;
		result.color = 0x000000;
		
		// définition de la branche de gauche
		result.left = new Tree(); //Création de la branche gauche
		result.left.length = 80;
		result.left.thickness = 8;
		result.left.angle = -Math.PI /8;
		result.left.left = null;
		result.left.right = null;
		result.left.color = 0x000000;
		
		// définition de la branche de droite
		Tree right = new Tree(); // Création d'un nouveau noeud
		right.length=70;
		right.thickness=8;
		right.angle = Math.PI/10;
		right.left = null;
		right.right = null;
		right.color = 0x000000;
		result.right = right; //affectation du noeud créé à la branche droite
		
		return result;
	}
	
	void bloom(Tree tree){
		// Cette fonction doit faire pousser l'arbre.
		// pour cela, il faut transformer les feuilles
		// en leur ajoutant un fils gauche et un fils droit.
		// Les valeurs conseillés sont :
		// Longueur et largeur des fils : 80% de la feuille
		// angle fils gauche : -PI/8
		// angle fils droit : +PI/10
		// Vous pouvez bien sur essayer d'autres valeurs.
		// Remarques :
		// - Cette fonction n'est jamais appelée pour tree==null
		// - Vous devez modifier directement l'arbre. Il n'y a aucune
		//   valeur à retourner.
		
	}

	void enlarge(Tree tree) {
		// Cette fonction doit agrandir chaque segment (noeud) de l'arbre.
		// Pour l'instant, elle ne modifie que le tronc
		if (tree != null){
			tree.length = tree.length+10;
		}	
	}
	
	void reduce(Tree tree) {
		// Même chose que enlarge, mais dans l'autre sens
		if (tree != null){
			if(tree.length>10) tree.length = tree.length-10;			
		}	
	}
	
	Tree copy(Tree tree) {
		// Cette fonction doit copier l'arbre, de manière à ce que l'arbre
		// retourné ne soit pas modifié quand on touche à l'arbre original... 
		// (cette fonction ne fonctionne pas correctement en l'état)
		return tree;
	}
	
	void color(Tree tree){
		// Cette fonction doit colorier le premier tiers de l'arbre en marron, 
		// les feuilles en vert, et le reste en vert foncé.
		// actuellement, cette fonction met le tronc en vert.
		// Indice : il peut être utile de créer une fonction hauteur pour
		// calculer la hauteur de l'arbre, et une autre pour réaliser le coloriage
		// proprement dit -- et utilisant plus de paramètres que celle-ci.
		tree.color = 0x00FF00;		
	}
	
	
	public static void  main(String[] arg){
		
		
		
		DessinArbre.common();
	}



}
