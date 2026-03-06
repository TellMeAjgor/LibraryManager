package pl.libraryManager.controller;

public enum MenuCommand {
    LIST_BOOKS("1"),
    SEARCH_BOOKS("2"),
    BORROW_BOOK("3"),
    RETURN_BOOK("4"),
    MY_LOANS("5"),
    CATEGORIES("6"),
    ADD_BOOK("7"),
    EDIT_BOOK("8"),
    DELETE_BOOK("9"),
    ADD_CATEGORY("10"),
    LOGOUT("0"),
    UNKNOWN("");

    private final String code;

    MenuCommand(String code) {
        this.code = code;
    }

    public static MenuCommand fromCode(String code) {
        for (MenuCommand cmd : values()) {
            if (cmd.code.equals(code)) {
                return cmd;
            }
        }
        return UNKNOWN;
    }
}