import java.util.List;

class A002_Elevators{

    public static void main(String[] args) {
        
    }

    /****************************************/

    // Busco la distancia entre el ascensor y el piso buscado
    private static int findDistance(Elevator elevator, int floorNumber) {
        int distance = Integer.MAX_VALUE;

        if (elevator.state != 0){    // Si se está moviendo, tengo que calcular la cantidad de pisos hasta donde tiene que ir + hasta el piso de donde lo llamo
            int firstTrip = elevator.getNextFloor() - elevator.getFloor();
            distance = firstTrip + (elevator.getNextFloor() - floorNumber);
        }else{
            distance = elevator.getFloor();
        }

        return distance - floorNumber;  // Veo a donde está o donde está yendo el ascensor
    }

    /****************************************/

    class Floor{
        private int floorNumber;

        public Floor(){}

        public int getFloorId(){ return this.floorNumber; }

        // Funcion que se llama desde un piso para llamar a un ascensor
        public void callElevator(List<Elevator> elevators){
            int floorNumber = this.getFloorId();                                        // Numero de piso desde donde llaman
    
            Elevator closerElevator = findCloserElevator(elevators, floorNumber);       // Busco el ascensor más cercano al piso
        
            closerElevator.call(floorNumber);                                           // Llamo al ascensor    
        }

        // Función para encontrar el ascensor que llegue más rápido al piso
        public Elevator findCloserElevator(List<Elevator> elevators, int floorNumber){

            Elevator closerElevator = null;
            int minDistance = Integer.MAX_VALUE;

            for (Elevator elevator : elevators) {
                int distance = findDistance(elevator,floorNumber);
                if (distance < minDistance) closerElevator = elevator;  
            }

            return closerElevator;    
        }
    }

    /****************************************/

    class Elevator{
        private int id;         // Numero de ascensor
        private int state;      // Subiendo (1), bajando (-1) o frenado (0)
        private int floor;      // Numero de piso en donde se encuentra, si es que está esperando
        private boolean door;   // Abrir o cerrar puerta
        private int nextFloor;  // a donde está yendo

        public Elevator(){}

        public int getId(){                 return this.id; }
        public int getState(){              return this.state; }
        public int getFloor(){              return this.floor; }
        public boolean getDoor(){           return this.door; }
        public int getNextFloor(){          return this.nextFloor; }

        public void setState(int state){    this.state = state; }
        public void setFloor(int floor){    this.floor = floor; }
        public void setDoor(boolean door){  this.door = door; }
        public void setNextFloor(int next){ this.nextFloor = next; }

        public void call(int floor){
            setDoor(false);         // Cierro la puerta
            int newState = floor > getFloor()? 1:-1;    
            setState(newState);     // Si el piso está más arriba del original pongo subiendo (1), sino bajando (-1)
            start(floor);           // Arranco el motor
            setNextFloor(floor);
        }

        private void start(int floor) {
            /* Hago todo para ir al piso */
            while(findDistance(this, floor) != 0){}

            // Llegó al piso
            setFloor(floor);    // El ascensor ahora está en el piso "floor"
            setDoor(true);      // Abro la puerta
            setState(0);        // Lo pongo como frenado
        }
    }

    /****************************************/

}

