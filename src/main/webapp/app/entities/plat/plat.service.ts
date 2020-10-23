import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPlat } from 'app/shared/model/plat.model';

type EntityResponseType = HttpResponse<IPlat>;
type EntityArrayResponseType = HttpResponse<IPlat[]>;

@Injectable({ providedIn: 'root' })
export class PlatService {
  public resourceUrl = SERVER_API_URL + 'api/plats';

  constructor(protected http: HttpClient) {}

  create(plat: IPlat): Observable<EntityResponseType> {
    return this.http.post<IPlat>(this.resourceUrl, plat, { observe: 'response' });
  }

  update(plat: IPlat): Observable<EntityResponseType> {
    return this.http.put<IPlat>(this.resourceUrl, plat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
