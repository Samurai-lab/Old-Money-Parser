package org.example;

import java.util.ArrayList;
import java.util.List;

public class IPAddressesPool {
        private List<String> ipAddresses;
        private int index;

        // Инициализация пула IP-адресов
        public IPAddressesPool() {
            ipAddresses = new ArrayList<>();
            ipAddresses.add("37.19.220.179:8443");
            ipAddresses.add("143.244.60.111:8443");
            ipAddresses.add("143.244.60.116:8443");

            index = 0;
        }

    public synchronized String getNextIPAddress() {
        String ipAddress = ipAddresses.get(index);
        index = (index + 1) % ipAddresses.size();
        return ipAddress;
    }

}
