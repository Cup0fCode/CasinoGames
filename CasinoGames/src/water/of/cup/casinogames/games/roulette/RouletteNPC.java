package water.of.cup.casinogames.games.roulette;

import water.of.cup.boardgames.game.npcs.GameNPC;
import water.of.cup.boardgames.game.npcs.NPCSkin;

public class RouletteNPC extends GameNPC {

    private static final String skinSig = "T5QGS3fQ9wWvsjmD6l9b/nZMkfOfYW1X3c1xvDdZQ5WHvPmew//3Q86+yfgQqIjPvEcXiDilr71p3WDrz/itsLb5mf9wLU5P4X18x5c6bmmv49TDLUCH5mEIUXu1jiQ8Kog/vzZNGZAAxadTGQPJ7BdII/+OpHDLS+WiCPRMnjCs/1h5RTE7I1OOPQnsh+yk+gOpaxCxgVFMLnMqNnL3mJP05qajHI6OKKXnyyXPwV0xxA3XT2WPbtCPsux3CjNCPP7fA1mYL4dPtdTaju9kP+6jeuf0IkS0jZ31bHKx324cM/W4xiSbR/2OSyYepHdS7TxWPZIYpkMPbaHMLXao7Ok209LD7p3GWZ5RDNvnZTcvGlF10wKoHJ9xy7lHoSfy4NfRAD3doATK5meRo7/JQCCo8M8Mw6dnBvYC9bcb3zCrvTkwQz2dfjkHvmH/QcWkJS5iqYCS6Uk67PJsFtYxa5a9ZBiZGUVxhprrB0hoZem0vfsnzGgzbwjpw0VxDSN1ndXSIJZ4yXB2KI58NE0HMjkVL9OcmOItoS4fqLqdo7CqrntdHsRcDZ7lSaCFVphBMsJI3AbrWAyIM54N9SSMJgpQkrbJ1tWhO1jp8mTXGqW1YlbmCEFS+LRR6sk/F3YK6FtSucJlhlrOdeKGHVaESWLVFzTMBgfVS3TfKSxSRI8=";
    private static final String skinData = "ewogICJ0aW1lc3RhbXAiIDogMTYxODIxNTU4MTc1OCwKICAicHJvZmlsZUlkIiA6ICJlZDUzZGQ4MTRmOWQ0YTNjYjRlYjY1MWRjYmE3N2U2NiIsCiAgInByb2ZpbGVOYW1lIiA6ICI0MTQxNDE0MWgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTA0NGNiODMyZDg3Y2RlNmFmNDJhMGRlNDdiYzg1YTY3YzdkNGU1OWEyZDc0NjY2MTc2ZDFjYTQxYWJkMGEyZCIKICAgIH0KICB9Cn0=";

    public RouletteNPC(double[] loc) {
        super(loc);
    }

    @Override
    protected String getName() {
        return "Dealer";
    }

    @Override
    protected NPCSkin getSkin() {
        return new NPCSkin(skinSig, skinData);
    }
}
