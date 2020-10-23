import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITypePlat } from 'app/shared/model/type-plat.model';

type EntityResponseType = HttpResponse<ITypePlat>;
type EntityArrayResponseType = HttpResponse<ITypePlat[]>;

@Injectable({ providedIn: 'root' })
export class TypePlatService {
  public resourceUrl = SERVER_API_URL + 'api/type-plats';

  constructor(protected http: HttpClient) {}

  create(typePlat: ITypePlat): Observable<EntityResponseType> {
    return this.http.post<ITypePlat>(this.resourceUrl, typePlat, { observe: 'response' });
  }

  update(typePlat: ITypePlat): Observable<EntityResponseType> {
    return this.http.put<ITypePlat>(this.resourceUrl, typePlat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypePlat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypePlat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
