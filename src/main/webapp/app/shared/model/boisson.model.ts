import { ICommande } from 'app/shared/model/commande.model';

export interface IBoisson {
  id?: number;
  libelle?: string;
  code?: string;
  typeId?: number;
  typeName?: string;
  commandes?: ICommande[];
}

export class Boisson implements IBoisson {
  constructor(
    public id?: number,
    public libelle?: string,
    public code?: string,
    public typeId?: number,
    public typeName?: string,
    public commandes?: ICommande[]
  ) {}
}
