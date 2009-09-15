package game;

import java.net.InetAddress;

public class Client
{
    private String _port;
    private InetAddress _ipAddress;
    private String _name;

    public Client(String port, InetAddress ipAddress, String name)
    {
        _port = port;
        _ipAddress = ipAddress;
        _name = name;
    }

    public int getPort()
    {
        return new Integer(_port);
    }

    public void setReceived(String port)
    {
        this._port = port;
    }

    public InetAddress getIPAddress()
    {
        return _ipAddress;
    }

    public void setAddress(InetAddress address)
    {
        this._ipAddress = address;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        this._name = name;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }

        Client client = (Client) o;

        if(_ipAddress != null ? !_ipAddress.equals(client._ipAddress) : client._ipAddress != null)
        {
            return false;
        }
        if(_name != null ? !_name.equals(client._name) : client._name != null)
        {
            return false;
        }
        if(_port != null ? !_port.equals(client._port) : client._port != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = _port != null ? _port.hashCode() : 0;
        result = 31 * result + (_ipAddress != null ? _ipAddress.hashCode() : 0);
        result = 31 * result + (_name != null ? _name.hashCode() : 0);
        return result;
    }

    public String toString()
    {
        return _port + " " + _ipAddress + " " + _name;
    }
}
