import { ICommande } from 'app/shared/model/commande.model';

export interface IPlat {
  id?: number;
  libelle?: string;
  code?: number;
  typeId?: number;
  typeName?: string;
  commandes?: ICommande[];
}

export class Plat implements IPlat {
  constructor(
    public id?: number,
    public libelle?: string,
    public code?: number,
    public typeId?: number,
    public typeName?: string,
    public commandes?: ICommande[]
  ) {}
}
