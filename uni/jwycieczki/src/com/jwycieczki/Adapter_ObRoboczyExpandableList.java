package com.jwycieczki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class Adapter_ObRoboczyExpandableList extends BaseExpandableListAdapter {
	private Context _context;
	private ArrayList<String> naglowki; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<WierszExpList>> listaTresci;

	public Adapter_ObRoboczyExpandableList(Context context, Uzytkownik wlasciciel,
			ArrayList<Wiadomosc>wiadomosci) {
		this._context = context;
		this.naglowki= new ArrayList<String>();
		this.listaTresci = new HashMap<String, List<WierszExpList>>();
		for (Wiadomosc w : wiadomosci){
			if (w.odbiorca.equals(wlasciciel.identyfikator)){// jesli wysylamy do listy osob w kazdej rozmowie pojawia sie wpis
				//for (Uzytkownik u : w.odbiorcy){
					if (!listaTresci.containsKey(w.nadawca)){ // sprawdzenie czy lista 
						naglowki.add(0,w.nadawca); //uzytkownikow juz istnieje w naglowkach
						ArrayList<WierszExpList> daneWiadomosci= new ArrayList<Adapter_ObRoboczyExpandableList.WierszExpList>();
						daneWiadomosci.add(0,new WierszExpList(w));
						listaTresci.put(w.nadawca,daneWiadomosci);
					}
					else{
						ArrayList<WierszExpList> daneWiadomosci= new ArrayList<Adapter_ObRoboczyExpandableList.WierszExpList>();
						daneWiadomosci.addAll(listaTresci.get(w.nadawca));
						daneWiadomosci.add(0,new WierszExpList(w));
						listaTresci.put(w.nadawca,daneWiadomosci);
						if (!naglowki.get(0).equals(w.nadawca)){
							int poz=naglowki.indexOf(w.nadawca); 
							naglowki.set(poz, naglowki.get(0));
							naglowki.set(0, w.nadawca);
							
						}
					}
					
				//}
			}
			else{
				if (!listaTresci.containsKey(w.odbiorca)){ // sprawdzenie czy byla rozmowa z odbiorcaem 
					naglowki.add(0,w.odbiorca);
					ArrayList<WierszExpList> daneWiadomosci= new ArrayList<Adapter_ObRoboczyExpandableList.WierszExpList>();
					daneWiadomosci.add(0,new WierszExpList(w));
					listaTresci.put(w.odbiorca,daneWiadomosci);
				}
				else{
					ArrayList<WierszExpList> daneWiadomosci= new ArrayList<Adapter_ObRoboczyExpandableList.WierszExpList>();
					daneWiadomosci.addAll(listaTresci.get(w.odbiorca));
					daneWiadomosci.add(0,new WierszExpList(w));
					listaTresci.put(w.odbiorca,daneWiadomosci);
					
				}
			}
			
		}
		
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.listaTresci.get(this.naglowki.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final WierszExpList child = (WierszExpList) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.exp_list_item_ob_roboczy, null);
		}

		TextView tekstWiadomosci = (TextView) convertView
				.findViewById(R.id.tvTrescOelv);
		TextView odbiorcaWiadomosci = (TextView) convertView
				.findViewById(R.id.tvAdresatOelv);
		TextView dataWiadomosci = (TextView) convertView
				.findViewById(R.id.tvdataOelv);
		

		tekstWiadomosci.setText(child.tresc);
		odbiorcaWiadomosci.setText(child.nadawca);
		dataWiadomosci.setText((child.data).toString());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		//int count=this.listaTresci.get(naglowki.get(groupPosition).idGrupy).size();
		//return count;
		return this.listaTresci.get(this.naglowki.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.naglowki.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.naglowki.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	private class WierszExpList{
		public String nadawca;
		public String data;
		public String tresc;
		
		public WierszExpList(Wiadomosc w){
			this.nadawca=w.odbiorca;
			this.data=w.data;
			this.tresc=w.tresc;
		}
		
	}
	private class Naglowek{
		public String naglowek;
		public int idGrupy;
		
		public Naglowek(String naglowek, int idGrupy){
			this.naglowek=naglowek;
			this.idGrupy=idGrupy;
		}
		
	}

}
