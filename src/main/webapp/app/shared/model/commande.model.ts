import { Moment } from 'moment';
import { IPlat } from 'app/shared/model/plat.model';
import { IBoisson } from 'app/shared/model/boisson.model';

export interface ICommande {
  id?: number;
  numero?: number;
  date?: Moment;
  status?: string;
  serveurId?: number;
  serveurName?: string;
  plats?: IPlat[];
  boissons?: IBoisson[];
}

export class Commande implements ICommande {
  constructor(
    public id?: number,
    public numero?: number,
    public date?: Moment,
    public status?: string,
    public serveurId?: number,
    public serveurName?: string,
    public plats?: IPlat[],
    public boissons?: IBoisson[]
  ) {}
}
