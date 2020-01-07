export class Account {
    constructor(
        public uid: string,
        public nom: string,
        public prenom: string,
        public authorities: string[],
        public ressources: string[],
    ) { }
}
