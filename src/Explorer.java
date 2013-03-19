

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;


import canvas.Action;
import canvas.Arrow;
import canvas.ArrowValue;
import canvas.Canvas;
import canvas.Enregistrement;
import canvas.Grob;
import canvas.MovedArrowAction;
import canvas.TextBox;

public class Explorer {
	public String getName(Grob newDest) {
		String des ="null";
		if (newDest != null){
			des = "( ";
			boolean first = true;
			for( Map<Enregistrement, String> it : name.values()){
				String s =it.get(newDest);
				if (s!= null){
					if (!first){
						des = des+" , ";
					}
					first  = false;
					des = des +s;
				}
			}
			des = des+" )";
			if (first) {
				des = "null";
			}
		}
		return des;
	}
	
	public String getSourceName(Arrow arrow){
		Enregistrement.Field field =((ArrowValue.Center)  arrow.from).getField(); 
		String result = getName(field.getEnregistrement());
		if (!result.equals("null") ){
			result += ".";
		} else {
			result = "";
		}
		result += ((TextBox) field.label).text;
		return result;
	}
	
	private final class MovedAction implements MovedArrowAction {
	
		public void moved(Arrow arrow, Grob newDest) {


			System.out.println(getSourceName(arrow)+" <- "+
					getName(newDest)+" [ anciennement " +getName(arrow.to)+" ] ");
		}

	}

	Map<Object,Enregistrement> map; 
	Map<String,Map<Enregistrement,String> > name;
	Canvas canvas;
	public int y;

	public Explorer(Canvas canvas){
		this.canvas = canvas;
		map = new WeakHashMap<Object, Enregistrement>();
		name = new HashMap<String,Map<Enregistrement,String> >();
		y = 20;
	}

	public void refresh(){

		System.gc();
		canvas.element.retainAll(map.values());
		for ( Map.Entry<Object,Enregistrement> e : map.entrySet()){
			Object o = e.getKey();
			Enregistrement r = e.getValue();
			if (o.getClass() != String.class){
				Field[] tab = o.getClass().getDeclaredFields();

				r.clear();
				int x = r.getBoundingBox().x+200;
				int y = r.getBoundingBox().y;
				for(Field f : tab){
					if (f.getName().indexOf('$') == -1 && o.getClass() != String.class){
						try {
							if (f.getType() == o.getClass()){
								Object link =f.get(o);
								if (map.containsKey(link)){
									x = map.get(link).getBoundingBox().x;
									y = map.get(link).getBoundingBox().y+tab.length*30;
								} else {
									createInitialView(link,x,y);
									y = y +tab.length+30;
								}
							}
						} catch (IllegalArgumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
			update();
		}

	}


	public void update(){
		for ( Map.Entry<Object,Enregistrement> e : map.entrySet()){
			Object o = e.getKey(); 
			Enregistrement r = e.getValue();

			if (r.field.isEmpty() )// && (o.getClass() != String.class))
			{
				Field[] tab = o.getClass().getDeclaredFields();
				for(Field f : tab){
					if (f.getName().indexOf('$') == -1 && o.getClass() != String.class){
						try {
							if (f.getType() == o.getClass()){
								r.addFieldRecord(f.getName(), map.get(f.get(o)));
							} else {
								Object ob = f.get(o);
								String st = ob == null ? "null" : ob.toString();
								r.addStringRecord(f.getName(), st);
							}
						} catch (IllegalArgumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				r.moved();
			}
		}
	}

	public void createInitialView(Object o, int x, int y){
		if (o == null || (o.getClass() == String.class)) return;
		if (!map.keySet().contains(o)){
			Enregistrement e = new Enregistrement(x,y);
			e.arrowMovedAction = new MovedAction();
			/*			final Enregistrement myEnregistrement = e;
			e.setAction(new Action(){
				public void act() {
					boolean first = true;
					for(Map<Enregistrement,String> label : name.values()){
						String s =label.get(myEnregistrement);
						if (s!= null){
							System.out.print((first ? "" : ", ")+s);
							first = false;
						}
					}
					System.out.println();
				}

			}); */
			map.put(o, e);
			canvas.add(e);

			Field[] tab = o.getClass().getDeclaredFields();
			for(Field f : tab){
				if ((f.getType() == o.getClass()) && (f.getName().indexOf('$') == -1)){
					try {
						createInitialView(f.get(o),x+200,y);
						y = y+tab.length*30;
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		}
	}

	public void read(Object o,String label){


		
		if (o!= null) {
			createInitialView(o,150,y);
		}
		//		update();


		Enregistrement r = null;
		if (o != null){
			r = map.get(o);
		}

		Enregistrement e = map.get(label);
		if (e == null){
			e = new Enregistrement(20,y);
			e.arrowMovedAction = new MovedAction();
			e.addFieldRecord(label, r);
			canvas.add(e);
			y = y +e.getBoundingBox().height+20;
			map.put(label,e);
		} else {
			e.clear();
			e.addFieldRecord(label, r);
		}
		e.field.firstElement().label.setBackground(new Color(0xFAFFE0));


		if (o != null) makeNames(label,o);

		canvas.repaint();


	}

	private void makeNames(String label,Object o) {
		Map<Enregistrement, String> nameMap =name.get(o);
		if (nameMap == null){
			nameMap = new HashMap<Enregistrement,String>();
			name.put(label, nameMap);
		} else {
			nameMap.clear();
		}
		Map<Object, String> toDo = new HashMap<Object, String>();
		toDo.put(o,label);
		Map<Object,String> next = new HashMap<Object, String>();


		while (!toDo.isEmpty()){
			for( Entry<Object,String> e : toDo.entrySet()){
				nameMap.put(map.get(e.getKey()), e.getValue());
				for( Field f : e.getKey().getClass().getDeclaredFields()){
					if (f.getType() == e.getKey().getClass()){
						Object newObject;
						try {
							newObject = f.get(e.getKey());
						} catch (Exception e1) {
							e1.printStackTrace();
							newObject = null;
						}
						if (newObject !=null && map.containsKey(newObject) && 
								!nameMap.containsKey(map.get(newObject))){
							next.put(newObject,e.getValue()+"."+f.getName());
						}
					}
				}
			}
			toDo = next;
			next = new HashMap<Object, String>();
		}
	}
}
