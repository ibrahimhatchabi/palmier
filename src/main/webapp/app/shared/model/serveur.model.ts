export interface IServeur {
  id?: number;
  nom?: string;
  prenom?: string;
  telephone?: string;
  civiliteId?: number;
  civiliteName?: string;
}

export class Serveur implements IServeur {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public telephone?: string,
    public civiliteId?: number,
    public civiliteName?: string
  ) {}
}
