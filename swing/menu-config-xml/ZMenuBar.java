import java.io.File;
import java.util.HashMap;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ZMenuBar extends JMenuBar {
    public ZMenuBar(String configPath) {
        Node root = null;
        NodeList children = null;
        int childCnt;

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(configPath));
            doc.getDocumentElement().normalize();

            if (!doc.hasChildNodes()) {
                return;
            }

            children = doc.getChildNodes();
            childCnt = children.getLength();
            for (int i = 0; childCnt > i; i++) {
                Node child = children.item(i);
                String childName = child.getNodeName();
                if (null == childName) {
                    continue;
                }

                if (0 == childName.compareTo("menuconfig")) {
                    root = children.item(i);
                    break;
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if ((null == root) || (!root.hasChildNodes())) {
            return;
        }

        children = root.getChildNodes();
        childCnt = children.getLength();
        for (int i = 0; childCnt > i; i++) {
            JMenu menu = parseNode(children.item(i));
            if (null != menu) {
                this.add(menu);
            }
        }
    }

    private static HashMap<String, String> extractItem(Node node) {
        HashMap<String, String> dict = null;

        if (node.hasAttributes()) {
            dict = new HashMap<String, String>();
            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0; attributes.getLength() > i; i++) {
                Node attr = attributes.item(i);
                String tag = attr.getNodeName();
                String value = attr.getNodeValue();

                if ((null == tag) || tag.isEmpty()) {
                    continue;
                } else {
                    dict.put(tag, value);
                }
            }
        }

        return dict;
    }

    private static JMenu parseNode(Node node) {
        JMenu menu = null;
        HashMap<String, String> dict = null;

        dict = extractItem(node);

        if ((null != dict) && dict.containsKey("name")) {
            menu = new JMenu(dict.get("name"));
        }

        if ((null == menu) || (!node.hasChildNodes())) {
            return menu;
        }

        NodeList children = node.getChildNodes();
        final int childCnt = children.getLength();

        for (int i = 0; childCnt > i; i++) {
            Node child = children.item(i);
            dict = extractItem(child);

            if ((null == dict) || (!dict.containsKey("name"))) {
                continue;
            }

            if (child.hasChildNodes()) {
                JMenu subMenu = parseNode(child);
                if (null != subMenu) {
                    menu.add(subMenu);
                }
            } else {
                JMenuItem menuItem = new JMenuItem(dict.get("name"));
                final String cmd = dict.get("cmd");
                menuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        System.out.println(String.format("Command: `%s`", cmd));
                    }
                });
                menu.add(menuItem);
            }
        }

        return menu;
    }
}
