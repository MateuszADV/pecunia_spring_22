document.addEventListener("DOMContentLoaded", function () {
    // Telefon: +48 123 456 789
    Inputmask({ mask: "+48 999 999 999" }).mask("#phone");

    // Kod pocztowy: 12-345
    Inputmask({ mask: "99-999" }).mask("#zipCode");
});