package nu.epsilon.rss.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pär Sikö
 */
public class FeedProxySelector extends ProxySelector {

    List<Proxy> proxies = new ArrayList<Proxy>();
    private Logger logger = Logger.getLogger(
            "nu.epsilon.rss.io.FeedProxySelector");

    /**
     * Constructs a new proxy selector referencing proxy objects created from
     * proxyMap.
     *
     * @param proxyMap contains proxy address- and port.
     */
    public FeedProxySelector(Map<String, Integer> proxyMap) {
        if (proxyMap.isEmpty()) {
            proxies.add(Proxy.NO_PROXY);
        } else {
            for (String address : proxyMap.keySet()) {
                try {
                    proxies.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                            address, proxyMap.get(address))));
                } catch (Exception exception) {
                    logger.logp(Level.WARNING, this.getClass().toString(),
                            "FeedProxySelector",
                            "address or port is illegal", exception);
                }
            }
        }
    }

    @Override
    public List<Proxy> select(URI uri) {
        return proxies;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        logger.logp(Level.WARNING, "FeedProxySelector", "connectFailed",
                "Error connecting to proxy: " + uri.toString() +
                " socket address: " + sa, ioe);
    }
}
