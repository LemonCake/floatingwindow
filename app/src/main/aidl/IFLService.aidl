interface IFLService {
    void showWindow(in IBinder windowToken) = 1;

    void moveOffset(in IBinder windowToken, float offset) = 2;

    void removeWindow() = 3;
}
