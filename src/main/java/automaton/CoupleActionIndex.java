package main.java.automaton;

import main.java.data.Action;

	public class CoupleActionIndex{

		final static int NOT_SIGNIFICANT = -1;
		
		// L'action choisie
		private Action action;
		//Nous mène à l'index du tableau de decision
		private int index;

		/**
		 * action
		 * @param a
		 * index
		 * @param i
		 */
		public CoupleActionIndex(Action action, int index){
			this.action=action;
			this.index=index;
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
			if (this.getAction().equals(otherCoupleActionIndex.getAction()) && this.getIndex()==otherCoupleActionIndex.getIndex()){
				return true;
			} else {
				return false;
			}
		}
		
	}
	