package main.java.automaton;

import main.java.data.Action;

	public class CoupleActionIndex{

		final static int NOT_SIGNIFICANT = -1;
		public final static int SIGNIFICANT_BUT_NOT_TREATED_YET = -2;
		public final static double NON_SIGNIFICANT_QUALITY = -1.0;
		// L'action choisie
		private Action action;
		//Nous mène à l'index du tableau de decision
		private int index;
		//La qualité du choix de cette action.
		private double quality;

		/**
		 * 
		 * @param action action
		 * 
		 * @param index index
		 * 
		 * @param quality quality
		 * 
		 */
		public CoupleActionIndex(Action action, int index, double quality){
			this.setAction(action);
			this.setIndex(index);
			this.setQuality(quality);
		}
		public void setAction(Action action){
			this.action = action;
		}
		public Action getAction(){
			return this.action;
		}
		public void setIndex(int index){
			this.index=index;
		}
		public int getIndex(){
			return this.index;
		}
		public void copy(CoupleActionIndex src){
			this.action.copy(src.action);
			this.index=src.index;
			this.quality=src.quality;
		}
		public double getQuality(){
			return this.quality;
		}
		public void setQuality(double quality){
			this.quality=quality;
		}
		/**
		 * Les couples sont initialisés a avec un index de -1 pour que la memoire soit alloué, mais la valeur contenu n'a de sens que si cet index a été modifié par une autre valeur
		 * @return
		 */
		public boolean isSignificant(){
			return this.index!=NOT_SIGNIFICANT;
		}
		/**
		 * Met l'index de branchement a -1 pour signifier que le couple n'est pas une donnée pertinente
		 */
		public void setNonSignificant(){
			this.index=NOT_SIGNIFICANT;
		}
		public boolean equals(CoupleActionIndex otherCoupleActionIndex){
			if (this.getAction().equals(otherCoupleActionIndex.getAction()) && this.getIndex()==otherCoupleActionIndex.getIndex() && this.getQuality()==otherCoupleActionIndex.getQuality()){
				return true;
			} else {
				return false;
			}
		}
		
	}
	