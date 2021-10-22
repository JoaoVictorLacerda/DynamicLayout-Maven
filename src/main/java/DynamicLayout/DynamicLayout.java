package DynamicLayout;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DynamicLayout implements LayoutManager2 {

    /**
     * @author João Victor Lacerda de Queiroz
     */
    private ArrayList<Component> elementos = new ArrayList();

    private ArrayList<Byte> percentsWid = new ArrayList();
    private ArrayList<Byte> percentsHeig = new ArrayList();
    private ArrayList<Byte> percentsPositionsX = new ArrayList();
    private ArrayList<Byte> percentsPositionsY = new ArrayList();
    private ArrayList<Byte> percentsFonts = new ArrayList();

    int width;
    int height;

    public DynamicLayout(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addLayoutComponent(Component comp, Object constraints) {

        this.addLayoutComponent((String)constraints, comp);
    }

    /** @deprecated */
    @Deprecated
    public void addLayoutComponent(String name, Component comp) {
        synchronized(comp.getTreeLock()) {
            /**
             * Esse array de ELEMENTOS é responsável por guardar todos os filhos
             * de um pai que implementou o DynamicLayout.DynamicLayout (Seja um JFrame ou um JPanel).
             *
             * Com esses componentes armazenados em um arrayList, é possível trabalhar
             * com seus tamanhos e posições separadamente
             */
            this.elementos.add(comp);

            /**
             * Os outros ArrayLists são usados no armazenamento da porcentagem
             * de um x elemento de acordo com o seu elemento pai.
             *
             * Basicamente, o gerenciador funciona em cima disso!
             * Se um elemento filho que tem a largura de 10px e está dentro de um elemento pai
             * que possui 100px de largura, logo, pode-se entender que o elemento filho ocupa
             * 10% do elemento pai. Quando o pai é redimensionado, a porcentagem não mudará,
             * o elemento filho ainda possuirá 10% do pai; porém, quando o pai é redimensionado
             * os 10% do filho não valerá mais 10.
             *
             * Ex: 10% de 100 = 10;
             *     10% de 200 = 20.
             *
             * E o mesmo vale para as posições x e y
             *
             */
            this.percentsWid.add((byte) this.getPercent(this.width, comp.getWidth()));
            this.percentsHeig.add((byte) this.getPercent(this.height, comp.getHeight()));
            this.percentsPositionsX.add((byte) this.getPercent(this.width, (int)comp.getLocation().getX()));
            this.percentsPositionsY.add((byte) this.getPercent(this.height, (int)comp.getLocation().getY()));
            this.percentsFonts.add((byte) this.getPercent(comp.getWidth(), comp.getFont().getSize()));
        }
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(2147483647, 2147483647);
    }

    public void layoutContainer(Container target) {
        int height = target.getHeight();
        int width = target.getWidth();
        int cont = 0;

        for(Iterator elemento = this.elementos.iterator(); elemento.hasNext(); ++cont) {
            Component c = (Component)elemento.next();
            int widthFinal = this.geraTamanhoWid(width, cont);
            int heightFinal = this.geraTamanhoHeig(height, cont);
            c.setBounds(this.geraPositionX(width, cont), this.geraPositionY(height, cont), widthFinal, heightFinal);

            if(c.getFont() != null){
                Font font = c.getFont();
                int tamanhoFontFinal = this.geraTamanhoFont(widthFinal, cont);
                if(tamanhoFontFinal >= (heightFinal-8)){
                    tamanhoFontFinal = heightFinal-13;
                }
                c.setFont(new Font(font.getFontName(),font.getStyle(), tamanhoFontFinal));

                font= null;
            }
        }

    }

    private int getPercent(int tamPai, int tamFilho) {
        return tamFilho * 100 / tamPai;
    }

    private int geraPositionX(int tamPai, int cont) {
        return this.percentsPositionsX.get(cont) * tamPai / 100;
    }

    private int geraPositionY(int tamPai, int cont) {
        return this.percentsPositionsY.get(cont) * tamPai / 100;
    }

    private int geraTamanhoWid(int tamPai, int cont) {

        return this.percentsWid.get(cont) * tamPai / 100;
    }

    private int geraTamanhoHeig(int tamPai, int cont) {
        return this.percentsHeig.get(cont) * tamPai / 100;
    }

    private int geraTamanhoFont(int tamPai, int cont){
        return this.percentsFonts.get(cont) * tamPai / 100;
    }

    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    public void invalidateLayout(Container target) {
    }

    public float getLayoutAlignmentX(Container parent) {
        return 0.0F;
    }

    public float getLayoutAlignmentY(Container parent) {
        return 0.0F;
    }

    public void removeLayoutComponent(Component comp) {
    }
}


